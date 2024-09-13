package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;


import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;


public class MainPage {
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement statistics = $("[id='stat']");
    private final SelenideElement history = $("[id='spendings']");
    private final SelenideElement menuButton = $("[aria-label='Menu']");
    private final SelenideElement profileButton = $("[href='/profile']");

    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
    }

    public MainPage checkStatisticsBlockVisible() {
        statistics.shouldBe(visible);
        return this;
    }

    public MainPage checkStatisticsNotExist() {
        statistics.shouldBe(not(exist));
        return this;
    }

    public MainPage checkHistoryBlockVisible() {
        history.shouldBe(visible);
        return this;
    }

    public MainPage checkHistoryNotExist() {
        history.shouldBe(not(exist));
        return this;
    }


    public MainPage menuButtonClick() {
        menuButton.click();
        return this;
    }

    public MainPage profileButtonClick() {
        profileButton.click();
        return this;
    }



}
