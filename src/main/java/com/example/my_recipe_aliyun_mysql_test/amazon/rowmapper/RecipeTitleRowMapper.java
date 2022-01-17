package com.example.my_recipe_aliyun_mysql_test.amazon.rowmapper;

import com.example.my_recipe_aliyun_mysql_test.amazon.AmazonClient;
import com.example.my_recipe_aliyun_mysql_test.model.RecipeTitle;
import com.example.my_recipe_aliyun_mysql_test.model.RecipeTitleUI;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RecipeTitleRowMapper implements RowMapper<RecipeTitleUI> {

    private final AmazonClient amazonClient;

    public RecipeTitleRowMapper(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    @Override
    public RecipeTitleUI mapRow(ResultSet rs, int rowNum) throws SQLException {

        RecipeTitleUI recipeTitle = new RecipeTitleUI();

        String signedURL =amazonClient.generateCloudFrontPreSignedURL(rs.getString("image"));
        recipeTitle.setDescription(rs.getString("description"));
        recipeTitle.setImage(signedURL);
        recipeTitle.setTitle(rs.getString("title"));

        return recipeTitle;
    }
}
