package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

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
}
