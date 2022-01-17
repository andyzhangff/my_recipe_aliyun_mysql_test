package com.example.my_recipe_aliyun_mysql_test.controller;

import com.example.my_recipe_aliyun_mysql_test.amazon.AmazonClient;
import com.example.my_recipe_aliyun_mysql_test.dao.RecipeTitleDaoImpl;
import com.example.my_recipe_aliyun_mysql_test.model.AddRecipeTitle;
import com.example.my_recipe_aliyun_mysql_test.model.RecipeTitle;
import com.example.my_recipe_aliyun_mysql_test.model.RecipeTitleUI;
import com.example.my_recipe_aliyun_mysql_test.util.BASE64DecodedMultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@RestController
@RequestMapping("/api")
public class RecipeController {

    private final RecipeTitleDaoImpl recipeTitleDao;
    private final AmazonClient amazonClient;

    public RecipeController(RecipeTitleDaoImpl recipeTitleDao, AmazonClient amazonClient) {
        this.recipeTitleDao = recipeTitleDao;
        this.amazonClient = amazonClient;
    }

    @GetMapping("/get-recipe")
    public ResponseEntity<?> getRecipe() {
        RecipeTitleUI recipes = recipeTitleDao.getRecipe();
        return ResponseEntity.accepted().body(recipes);
    }

    @PostMapping("/add-recipe")
    public void addRecipe(@RequestBody AddRecipeTitle recipe) {

        String image = recipe.getImage();
        String imageFileName = recipe.getImageFileName();
        String awsS3FolderName = "testFolder";

        String imageObjectKeyOld = recipeTitleDao.getRecipeImageObjectKey();
        amazonClient.deleteFileFromS3Bucket(imageObjectKeyOld);

        uploadSingleFileToAWS3(image, awsS3FolderName, imageFileName);
        String imageObjectKeyNew = this.amazonClient.simulateObjectKey(awsS3FolderName, imageFileName);

        recipeTitleDao.removeRecipe();

        RecipeTitle recipeToAdd = new RecipeTitle();
        recipeToAdd.setDescription(recipe.getDescription());
        recipeToAdd.setTitle(recipe.getTitle());
        recipeToAdd.setImage(imageObjectKeyNew);
        recipeToAdd.setTime(0);
        recipeToAdd.setOwner("andyzhangff");
        recipeToAdd.setAws_folder_name("testFolder");

        recipeTitleDao.addRecipe(recipeToAdd);
    }

    public void uploadSingleFileToAWS3(String base64String, String folderName, String fileName) {
        MultipartFile multipartFile = convertBase64toMultipartFile(base64String, fileName);
        this.amazonClient.uploadSingleFile(multipartFile, folderName, fileName);
    }

    public MultipartFile convertBase64toMultipartFile(String base64String, String fileName) {
        byte[] decodeImageBinary = Base64.getDecoder().decode(base64String);
        return new BASE64DecodedMultipartFile(decodeImageBinary, fileName);
    }

}
