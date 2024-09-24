package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.CategoryDbClient;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

import static guru.qa.niffler.utils.RandomDataUtils.*;

public class SpendingExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);

    private final SpendDbClient spendDbClient = new SpendDbClient();
    private final CategoryDbClient categoryDbClient = new CategoryDbClient();


    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                    if (userAnno.spendings().length > 0) {
                        Spending spending = userAnno.spendings()[0];

                        CategoryJson category = new CategoryJson(
                                null,
                                spending.category(),
                                userAnno.name(),
                                false
                        );
                        CategoryJson createdCategory = categoryDbClient.createCategoryJson(category);

                        SpendJson spend = new SpendJson(
                                null,
                                new Date(),
                                createdCategory,
                                CurrencyValues.RUB,
                                spending.amount(),
                                spending.description(),
                                userAnno.name()
                        );
                        context.getStore(NAMESPACE).put(
                                context.getUniqueId(),
                                spendDbClient.createSpend(spend)
                        );
                    }
                });

    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {

        SpendJson spend = context.getStore(NAMESPACE).get(context.getUniqueId(), SpendJson.class);
        if (spend != null) {
            spendDbClient.deleteSpendJson(spend);
            categoryDbClient.deleteCategoryJson(spend.category());
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
    }

    @Override
    public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), SpendJson.class);
    }
}
