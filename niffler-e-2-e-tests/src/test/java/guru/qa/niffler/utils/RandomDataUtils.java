package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

public class RandomDataUtils {
    private static final Faker faker = new Faker();

    public static String randomCategoryName() {
        return faker.beer().name()+ faker.number().numberBetween(1, 999999);
    }
}
