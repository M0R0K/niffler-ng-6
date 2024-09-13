package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegistrationPage {

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement confirmPasswordInput = $("input[name='passwordSubmit']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement error = $("[class='form__error']");
    private final SelenideElement success = $("[class*='_success']");
    private final SelenideElement signInButton = $("[class='form_sign-in']");


    public RegistrationPage checkTextError(String errorText) {
        error.shouldHave(text(errorText));
        return this;


    }

    public RegistrationPage fillRegistrationForm(String username, String password, String confirmPassword) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        confirmPasswordInput.setValue(confirmPassword);
        return this;
    }

    public RegistrationPage submit() {
        submitButton.click();
        return this;
    }

    public RegistrationPage checkSuccess(String text) {
        success.shouldHave(text(text));
        return this;
    }

    public RegistrationPage checkSignInButton() {
        signInButton.shouldBe(visible);
        return this;
    }


}
