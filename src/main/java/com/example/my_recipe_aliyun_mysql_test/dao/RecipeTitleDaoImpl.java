package com.example.my_recipe_aliyun_mysql_test.dao;

import com.example.my_recipe_aliyun_mysql_test.amazon.AmazonClient;
import com.example.my_recipe_aliyun_mysql_test.model.RecipeTitle;
import com.example.my_recipe_aliyun_mysql_test.model.RecipeTitleUI;
import com.example.my_recipe_aliyun_mysql_test.amazon.rowmapper.RecipeTitleRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class RecipeTitleDaoImpl implements RecipeTitleDao{

    private final NamedParameterJdbcTemplate jdbcTemplateNamedParameter;
    private final JdbcTemplate jdbcTemplate;
    private final AmazonClient amazonClient;

    public RecipeTitleDaoImpl(NamedParameterJdbcTemplate jdbcTemplateNamedParameter, JdbcTemplate jdbcTemplate, AmazonClient amazonClient) {
        this.jdbcTemplateNamedParameter = jdbcTemplateNamedParameter;
        this.jdbcTemplate = jdbcTemplate;
        this.amazonClient = amazonClient;
    }

    @Override
    public RecipeTitleUI getRecipe() {

        String sql = "SELECT recipes_title.image,\n" +
                " recipes_title.title,\n" +
                "recipes_title.description\n" +
                "from recipes_title;";

        return jdbcTemplate.queryForObject(sql, new RecipeTitleRowMapper(amazonClient));
    }

    @Override
    public void removeRecipe() {
        final String sql = "DELETE FROM recipes_title;";

        jdbcTemplate.update(sql);
    }

    @Override
    public void addRecipe(RecipeTitle recipe) {

        final String sql = "INSERT INTO recipes_title ( title, description, image, owner, time, aws_folder_name)\n" +
                "values(:title, :description, :image, :owner, :time, :aws_folder_name)";

        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("title", recipe.getTitle())
                .addValue("description", recipe.getDescription())
                .addValue("owner", recipe.getOwner())
                .addValue("time", recipe.getTime())
                .addValue("aws_folder_name", recipe.getAws_folder_name())
                .addValue("image", recipe.getImage());

        jdbcTemplateNamedParameter.update(sql, param, holder);
    }

    @Override
    public String getRecipeImageObjectKey() {
        String sql = "SELECT image FROM recipes_title;";
        return jdbcTemplate.queryForObject(sql, String.class);
    }

}
