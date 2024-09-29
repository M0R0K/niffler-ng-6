package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.model.CategoryJson;

import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;

public class CategoryDbClient {
    //  private final CategoryDao categoryDao = new CategoryDaoJdbc();

    private static final Config CFG = Config.getInstance();

    public CategoryJson createCategoryJson(CategoryJson category) {
        return transaction(connection -> {
            // Проверяем, существует ли категория с данным именем и username
            Optional<CategoryEntity> existingCategory = new CategoryDaoJdbc(connection)
                    .findCategoryByUsernameAndCategoryName(category.username(), category.name());

            if (existingCategory.isPresent()) {
                return CategoryJson.fromEntity(existingCategory.get());
            } else {
                // Создаем новую категорию
                CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                return CategoryJson.fromEntity(new CategoryDaoJdbc(connection).create(categoryEntity));
            }
        }, CFG.spendJdbcUrl());
    }

    public CategoryEntity createCategory(CategoryEntity category) {
        return transaction(connection -> {
            return new CategoryDaoJdbc(connection).create(category);
        }, CFG.spendJdbcUrl());
    }

    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return transaction(connection -> {
            return new CategoryDaoJdbc(connection).findCategoryById(id);
        }, CFG.spendJdbcUrl());
    }

    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return transaction(connection -> {
            return new CategoryDaoJdbc(connection).findCategoryByUsernameAndCategoryName(username, categoryName);
        }, CFG.spendJdbcUrl());
    }

    public void deleteCategory(CategoryEntity category) {
        transaction(connection -> {
            new CategoryDaoJdbc(connection).deleteCategory(category);
        }, CFG.spendJdbcUrl());
    }


    public void deleteCategoryJson(CategoryJson category) {
        transaction(connection -> {
            // Ищем категорию по имени пользователя и имени категории
            Optional<CategoryEntity> categoryEntity = new CategoryDaoJdbc(connection)
                    .findCategoryByUsernameAndCategoryName(category.username(), category.name());

            // Если категория найдена, удаляем её
            categoryEntity.ifPresent(entity -> new CategoryDaoJdbc(connection).deleteCategory(entity));
        }, CFG.spendJdbcUrl());
    }
}
