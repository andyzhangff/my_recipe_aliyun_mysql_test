package com.example.my_recipe_aliyun_mysql_test.model;

public class RecipeTitle {

    private int id;
    private String title;
    private String description;
    private String image;
    private String owner;
    private int time;
    private String aws_folder_name;

    public RecipeTitle() {
    }

    public RecipeTitle(int id, String title, String description, String image, String owner, int time, String aws_folder_name) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.owner = owner;
        this.time = time;
        this.aws_folder_name = aws_folder_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getAws_folder_name() {
        return aws_folder_name;
    }

    public void setAws_folder_name(String aws_folder_name) {
        this.aws_folder_name = aws_folder_name;
    }
}
