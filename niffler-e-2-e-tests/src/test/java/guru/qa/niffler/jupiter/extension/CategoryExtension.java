package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.CategoryDbClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;


public class CategoryExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final CategoryDbClient categoryDbClient = new CategoryDbClient();

    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                    if (userAnno.categories().length > 0) {
                        Category anno = userAnno.categories()[0];
                        // Создаем категорию со случайным именем
                        final String randomCategoryName = randomCategoryName();
                        boolean isArchived = anno.archived();
                        CategoryJson category = new CategoryJson(
                                null,
                                randomCategoryName,
                                userAnno.name(),
                                isArchived
                        );


                        CategoryJson createdCategory = categoryDbClient.createCategoryJson(category);

                        // Сохраняем категорию в контексте
                        context.getStore(NAMESPACE).put(context.getUniqueId(), createdCategory);
                    }
                });
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {

        Optional.ofNullable(context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class))
                .ifPresent(category -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    categoryDbClient.deleteCategory(categoryEntity);
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(CategoryExtension.NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }
}
