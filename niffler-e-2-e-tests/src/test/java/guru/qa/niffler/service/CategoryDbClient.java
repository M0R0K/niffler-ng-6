package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.model.CategoryJson;

import java.util.Optional;
import java.util.UUID;

public class CategoryDbClient {
    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    public CategoryJson createCategoryJson(CategoryJson category) {
        Optional<CategoryEntity> existingCategory = categoryDao.findCategoryByUsernameAndCategoryName(category.username(), category.name());
        if (existingCategory.isPresent()) {
            return CategoryJson.fromEntity(existingCategory.get());
        } else {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            return CategoryJson.fromEntity(categoryDao.create(categoryEntity));
        }
    }

    public CategoryEntity createCategory(CategoryEntity category) {
        return categoryDao.create(category);
    }

    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return categoryDao.findCategoryById(id);
    }

    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return categoryDao.findCategoryByUsernameAndCategoryName(username, categoryName);
    }

    public void deleteCategory(CategoryEntity category) {
        categoryDao.deleteCategory(category);
    }


    public void deleteCategoryJson(CategoryJson category) {
        // Ищем категорию по имени пользователя и имени категории
        Optional<CategoryEntity> categoryEntity = categoryDao.findCategoryByUsernameAndCategoryName(category.username(), category.name());

        // Если категория найдена, удаляем её
        categoryEntity.ifPresent(categoryDao::deleteCategory);
    }
}
