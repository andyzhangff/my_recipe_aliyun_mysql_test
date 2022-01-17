package com.example.my_recipe_aliyun_mysql_test.amazon;

import com.amazonaws.AmazonClientException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.util.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jets3t.service.CloudFrontService;
import org.jets3t.service.CloudFrontServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Security;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class AmazonClient {

    private AmazonS3 s3client;
    private TransferManager transferManager;
    @Value("${amazon.endpointUrl}")
    private String endpointUrl;
    @Value("${amazon.bucketName}")
    private String bucketName;
    @Value("${amazon.accessKey}")
    private String accessKey;
    @Value("${amazon.secretKey}")
    private String secretKey;

    @Value("${amazon.distributionDomain}")
    private String distributionDomain;

    @Value("${amazon.privateKeyFilePath}")
    private String privateKeyFilePath;

    @Value("${amazon.keyPairId}")
    private String keyPairId;

    private final Regions clientRegion = Regions.AP_EAST_1;
    private final String rootFolder = "test";
    private final String separator = "/";

    @PostConstruct
    private void initializeAmazon() {
        try {
            BasicAWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
            s3client = AmazonS3ClientBuilder.standard().withRegion(clientRegion)
                    .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
            transferManager = TransferManagerBuilder.standard()
                    .withS3Client(s3client)
                    .withMultipartUploadThreshold((long) (5 * 1024 * 1025))
                    .build();
        } catch (AmazonS3Exception ex) {
            ex.printStackTrace();
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + Objects.requireNonNull(multiPart.getOriginalFilename())
                .replace(" ", "_");
    }

    private void uploadFileTos3bucket(String fileName, File file, String folderName) {
        PutObjectRequest request = new PutObjectRequest(
                bucketName, rootFolder + separator + folderName + separator + fileName, file);
        Upload upload = transferManager.upload(request);
        try {
            upload.waitForCompletion();
        } catch (AmazonClientException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void uploadMultipleFile(List<MultipartFile> multipartFiles, String folderName) {
        try {
            for (MultipartFile multipartFile : multipartFiles) {
                File file = convertMultiPartToFile(multipartFile);
                String fileName = generateFileName(multipartFile);
                String fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
                uploadFileTos3bucket(fileName, file, folderName);
                boolean wasSuccessful = file.delete();
                if (!wasSuccessful) {
                    System.out.println("file delete was not successful.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadSingleFile(MultipartFile multipartFile, String folderName, String fileName) {
        try {
            File file = convertMultiPartToFile(multipartFile);
            uploadFileTos3bucket(fileName, file, folderName);
            boolean wasSuccessful = file.delete();
            if (!wasSuccessful) {
                System.out.println("file delete was not successful.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFileFromS3Bucket(String objectKey) {
        try {
        s3client.deleteObject(new DeleteObjectRequest(bucketName, objectKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String generateCloudFrontPreSignedURL(String objectKey) {
        try {
            // Set the preSigned URL to expire after one hour.
            Date expiration = new Date();
            long expTimeMillis = Instant.now().toEpochMilli();
            expTimeMillis += 1000 * 60 * 60;
            expiration.setTime(expTimeMillis);

            Security.addProvider(new BouncyCastleProvider());
            String policyResourcePath = "https://" + distributionDomain + "/" + objectKey;

            // Convert your DER files into a byte array.
            byte[] derPrivateKey = IOUtils.toByteArray(new ClassPathResource(
                    privateKeyFilePath).getInputStream());

            // Generate a "canned" signed URL to allow access to a
            // specific distribution and file
            String signedUrlCanned = CloudFrontService.signUrlCanned(
                    "https://" + distributionDomain + "/" + objectKey,
                    keyPairId,
                    derPrivateKey,
                    expiration
            );
            System.out.println(signedUrlCanned);
            return signedUrlCanned;
        } catch (SdkClientException | IOException | CloudFrontServiceException e) {
            e.printStackTrace();
        }
        return "something wrong with generate presigned url function";
    }

    public String simulateObjectKey(String folderName, String fileName) {
        return rootFolder + separator + folderName + separator + fileName;
    }

}
