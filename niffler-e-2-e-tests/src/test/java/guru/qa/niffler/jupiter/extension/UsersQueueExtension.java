package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.UserType;
import guru.qa.niffler.model.StaticUser;
import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;


import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);


    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();


    static {
        EMPTY_USERS.add(new StaticUser("empty", "123456", null, null, null));
        WITH_FRIEND_USERS.add(new StaticUser("withFriend", "123456", "friend", null, null));
        WITH_INCOME_REQUEST_USERS.add(new StaticUser("withIncome", "123456", null, "withIncome", null));
        WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("withOutcome", "123456", null, null, "withOutcome"));
    }


    private Queue<StaticUser> getQueueByUserType(UserType.Type type) {
        switch (type) {
            case EMPTY:
                return EMPTY_USERS;
            case WITH_FRIEND:
                return WITH_FRIEND_USERS;
            case WITH_INCOME_REQUEST:
                return WITH_INCOME_REQUEST_USERS;
            case WITH_OUTCOME_REQUEST:
                return WITH_OUTCOME_REQUEST_USERS;
            default:
                throw new IllegalArgumentException("Неверный тип пользователя " + type);
        }
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .forEach(p -> {
                    UserType userType = p.getAnnotation(UserType.class);
                    Queue<StaticUser> queue = getQueueByUserType(userType.value());
                    Optional<StaticUser> user = Optional.empty();
                    StopWatch sw = StopWatch.createStarted();

                    while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                        user = Optional.ofNullable(queue.poll());
                    }

                    Allure.getLifecycle().updateTestCase(testCase ->
                            testCase.setStart(new Date().getTime())
                    );
                    user.ifPresentOrElse(
                            u -> {
                                Map<UserType, StaticUser> usersMap = (Map<UserType, StaticUser>) context
                                        .getStore(NAMESPACE)
                                        .getOrComputeIfAbsent(context.getUniqueId(), key -> new HashMap<>());
                                usersMap.put(userType, u);
                            },
                            () -> {
                                throw new IllegalStateException("Can't obtain user after 30s.");
                            }
                    );
                });
    }


    @Override
    public void afterTestExecution(ExtensionContext context) {
        Map<UserType, StaticUser> map = context.getStore(NAMESPACE).get(
                context.getUniqueId(),
                Map.class
        );

        map.forEach((userType, user) -> {
            Queue<StaticUser> queue = getQueueByUserType(userType.value());
            queue.add(user);
        });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {

        Map<UserType, StaticUser> userMap = (Map<UserType, StaticUser>) extensionContext.getStore(NAMESPACE)
                .getOrComputeIfAbsent(extensionContext.getUniqueId(), key -> new HashMap<>());

        UserType userTypeAnnotation = parameterContext.findAnnotation(UserType.class)
                .orElseThrow(() -> new ParameterResolutionException("Аннотация @UserType не найдена"));

        return Optional.ofNullable(userMap.get(userTypeAnnotation))
                .orElseThrow(() -> new ParameterResolutionException("Пользователь не найден для указанного типа UserType"));
    }
}