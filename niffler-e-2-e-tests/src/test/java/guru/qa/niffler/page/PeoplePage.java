package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;


public class PeoplePage {
    private final ElementsCollection friendsRows = $$("[id='friends']");
    private final ElementsCollection requestsRows = $$("[id='requests']");
    private final ElementsCollection allRows = $$("[id='all']");
    private final SelenideElement acceptButton = $x("//button[text()='Accept']");
    private final SelenideElement declineButton = $x("//button[text()='Decline']");
    private final SelenideElement withoutFriendsText = $x("//p[text()='There are no users yet']");

    public PeoplePage shouldBePresentInFriendsTable(String friendName) {
        friendsRows.findBy(text(friendName)).shouldBe(visible);
        return this;
    }


    public PeoplePage shouldBePresentInRequestsTable(String friendName) {
        requestsRows.findBy(text(friendName)).shouldBe(visible);
        return this;
    }


    public PeoplePage shouldBePresentInAllPeopleTable(String friendName) {
        allRows.findBy(text(friendName)).shouldBe(visible);
        return this;
    }

    public PeoplePage shouldBePresentTextWaitingInvite (String message) {
        allRows.findBy(text(message)).shouldBe(visible);
        return this;
    }

    public PeoplePage shouldBePresentButtonsInRequest() {
        acceptButton.shouldBe(visible);
        declineButton.shouldBe(visible);
        return this;
    }

    public PeoplePage shouldBePresentWithoutFriendsText() {
        withoutFriendsText.shouldBe(exist);
        return this;
    }

}