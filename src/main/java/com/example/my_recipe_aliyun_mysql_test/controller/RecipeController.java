package com.example.my_recipe_aliyun_mysql_test.controller;

import com.example.my_recipe_aliyun_mysql_test.dao.RecipeTitleDaoImpl;
import com.example.my_recipe_aliyun_mysql_test.model.RecipeTitle;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeTitleDaoImpl recipeTitleDao;

    public RecipeController(RecipeTitleDaoImpl recipeTitleDao) {
        this.recipeTitleDao = recipeTitleDao;
    }

    @GetMapping("/get-recipes")
    public ResponseEntity<?> getRecipes() {
        List<RecipeTitle> recipes = recipeTitleDao.getRecipes();
        return ResponseEntity.accepted().body(recipes);
    }

}
