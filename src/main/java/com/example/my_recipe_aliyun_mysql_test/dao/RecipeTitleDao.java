package com.example.my_recipe_aliyun_mysql_test.dao;

import com.example.my_recipe_aliyun_mysql_test.model.RecipeTitle;
import com.example.my_recipe_aliyun_mysql_test.model.RecipeTitleUI;

public interface RecipeTitleDao {

    RecipeTitleUI getRecipe();

    void removeRecipe();

    void addRecipe(RecipeTitle recipe);

    String getRecipeImageObjectKey();

}
