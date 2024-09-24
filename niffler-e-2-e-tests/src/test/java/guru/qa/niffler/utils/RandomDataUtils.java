package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

public class RandomDataUtils {
    private static final Faker faker = new Faker();

    public static final String randomCategoryName() {
        return faker.beer().name() + faker.number().numberBetween(1, 999999);
    }
    public static final String randomDescription() {
        return faker.lorem().sentence();
    }
    public static final int randomAmount() {
        return faker.number().numberBetween(1, 100000);
    }
}
