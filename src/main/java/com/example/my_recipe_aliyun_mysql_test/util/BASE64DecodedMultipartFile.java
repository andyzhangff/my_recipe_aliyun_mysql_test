package com.example.my_recipe_aliyun_mysql_test.util;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.*;

public class BASE64DecodedMultipartFile implements MultipartFile {

    private final byte[] imgContent;
    private final String filename;

    public BASE64DecodedMultipartFile(byte[] imgContent, String filename) {
        this.imgContent = imgContent;
        this.filename = filename;
    }

    @NotNull
    @Override
    public String getName() {
        return filename.split("\\.")[0];
    }

    @Override
    public String getOriginalFilename() {
        return filename.split("\\.")[0];
    }

    @Override
    public String getContentType() {
        return filename.split("\\.")[1];
    }

    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize() {
        return imgContent.length;
    }

    @NotNull
    @Override
    public byte[] getBytes() throws IOException {
        return imgContent;
    }

    @NotNull
    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public void transferTo(@NotNull File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(imgContent);
    }

}
