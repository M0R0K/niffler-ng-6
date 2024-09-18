package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.UserType;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.StaticUser;
import guru.qa.niffler.page.PeoplePage;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.annotation.UserType.Type.WITH_FRIEND;
import static guru.qa.niffler.jupiter.annotation.UserType.Type.EMPTY;
import static guru.qa.niffler.jupiter.annotation.UserType.Type.WITH_INCOME_REQUEST;
import static guru.qa.niffler.jupiter.annotation.UserType.Type.WITH_OUTCOME_REQUEST;

@ExtendWith(BrowserExtension.class)
public class FriendsWebTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void testFriendShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) StaticUser user) {

        final String userInFriendList = "friend";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password()).menuButtonClick().friendsButtonClick();
        new PeoplePage().shouldBePresentInFriendsTable(userInFriendList);
    }

    @Test
    void testFriendsTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password()).menuButtonClick().friendsButtonClick();
           new PeoplePage().shouldBePresentWithoutFriendsText();
    }


    @Test
    void testIncomeInvitationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUser user) {

        final String userWithOutcome = "withOutcome";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password()).menuButtonClick().friendsButtonClick();
           new PeoplePage().shouldBePresentInRequestsTable(userWithOutcome).shouldBePresentButtonsInRequest();
    }

    @Test
    void testOutcomeInvitationBePresentInAllPeoplesTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
        final String userWithIncome = "WithIncome";
        final String userIncomeLabel = "Waiting...";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password()).menuButtonClick().allPeopleButtonClick();
           new PeoplePage().shouldBePresentInAllPeopleTable(userWithIncome).shouldBePresentTextWaitingInvite(userIncomeLabel);
    }
}


