package com.example.my_recipe_aliyun_mysql_test.dao;

import com.example.my_recipe_aliyun_mysql_test.model.RecipeTitle;
import com.example.my_recipe_aliyun_mysql_test.rowmapper.RecipeTitleRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecipeTitleDaoImpl implements RecipeTitleDao{

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public RecipeTitleDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<RecipeTitle> getRecipes() {

        String sql = "SELECT * from recipes_title;";

        return jdbcTemplate.query(sql, new RecipeTitleRowMapper());
    }

}
