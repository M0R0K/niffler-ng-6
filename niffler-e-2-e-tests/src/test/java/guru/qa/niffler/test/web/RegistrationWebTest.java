package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegistrationPage;
import org.junit.jupiter.api.Test;

public class RegistrationWebTest {

    protected final Faker faker = new Faker();

    private final String password = faker.internet().password(6, 8);
    private final String anotherPassword = faker.internet().password(9, 11);
    private final String username = faker.name().username();
    private final String secondUsername = faker.name().username();
    private final String existingUsername = "test";
    private final String errorTextUser = "Username `" + existingUsername + "` already exists";
    private final String errorTextPassword = "Passwords should be equal";

    private static final Config CFG = Config.getInstance();

    @Test
    void shouldRegisterNewUser() {
        final String successText = "Congratulations! You've registered!";

        Selenide.open(CFG.frontUrl(), LoginPage.class).goToCreateAccountPage();

        new RegistrationPage().fillRegistrationForm(username, password, password).submit().checkSuccess(successText).checkSignInButton();
    }

    @Test
    void shouldNotRegisterNewUserWithExistingUsername() {
        Selenide.open(CFG.frontUrl(), LoginPage.class).goToCreateAccountPage();

        new RegistrationPage().fillRegistrationForm(existingUsername, password, password).submit().checkTextError(errorTextUser);
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotMatch() {
        Selenide.open(CFG.frontUrl(), LoginPage.class).goToCreateAccountPage();
        new RegistrationPage().fillRegistrationForm(secondUsername, password, anotherPassword).submit().checkTextError(errorTextPassword);


    }

}
