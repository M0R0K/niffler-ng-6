package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

public class LoginWebTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        final String errorText = "Неверные учетные данные пользователя";

        Selenide.open(CFG.frontUrl(), LoginPage.class).login("test", "1234567");

        new LoginPage().checkError(errorText);
        new MainPage().checkHistoryNotExist().checkStatisticsNotExist();
    }

    @Test
    void mainPageShouldBeDisplayedAfterSuccessfulLogin() {
        Selenide.open(CFG.frontUrl(), LoginPage.class).login("test", "123456");
        new MainPage().checkStatisticsBlockVisible().checkHistoryBlockVisible();

    }


}
