package com.example.my_recipe_aliyun_mysql_test.rowmapper;

import com.example.my_recipe_aliyun_mysql_test.model.RecipeTitle;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RecipeTitleRowMapper implements RowMapper<RecipeTitle> {

    @Override
    public RecipeTitle mapRow(ResultSet rs, int rowNum) throws SQLException {

        RecipeTitle recipeTitle = new RecipeTitle();

        recipeTitle.setId(rs.getInt("id"));
        recipeTitle.setDescription(rs.getString("description"));
        recipeTitle.setImage(rs.getString("image"));
        recipeTitle.setTitle(rs.getString("title"));
        recipeTitle.setOwner(rs.getString("owner"));
        recipeTitle.setTime(rs.getInt("time"));
        recipeTitle.setAws_folder_name(rs.getString("aws_folder_name"));

        return recipeTitle;

    }
}
