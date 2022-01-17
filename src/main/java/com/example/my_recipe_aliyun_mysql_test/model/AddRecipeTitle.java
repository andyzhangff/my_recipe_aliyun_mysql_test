package com.example.my_recipe_aliyun_mysql_test.model;

public class AddRecipeTitle {

    private String title;
    private String description;
    private String image;
    private String imageFileName;

    public AddRecipeTitle() {
    }

    public AddRecipeTitle(String title, String description, String image, String imageFileName) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.imageFileName = imageFileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }
}
