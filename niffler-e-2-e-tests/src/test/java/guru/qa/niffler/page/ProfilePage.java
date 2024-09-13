package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;


public class ProfilePage {

    private final SelenideElement archiveButton = $("[aria-label='Archive category']");
    private final SelenideElement archiveButtonSubmit = $x("//button[text()='Archive']");
    private final SelenideElement successAlert = $x("//div[@class='MuiAlert-message css-1xsto0d']");
    private final SelenideElement showArchivedButton = $x("//span[text()='Show archived']");
    private final ElementsCollection categoryItem = $$("[class*='MuiChip-filled']");
    private final SelenideElement unarchiveButtonSubmit = $x("//button[text()='Unarchive']");


    public ProfilePage clickArchiveButton() {
        archiveButton.click();
        return this;
    }

    public ProfilePage clickUnarchiveButton() {
        unarchiveButtonSubmit.click();
        return this;
    }

    public ProfilePage clickArchiveButtonSubmit() {
        archiveButtonSubmit.click();
        return this;
    }

    public ProfilePage checkSuccessAlert(String message) {
        successAlert.shouldHave(text(message));
        return this;
    }

    public ProfilePage clickArchiveSwitcher() {
        showArchivedButton.click();
        return this;
    }

    public ProfilePage checkCategoryInlist(String categoryName) {
        categoryItem.findBy(text(categoryName)).should(exist);
        return this;
    }

    public ProfilePage checkCategoryNotInlist(String categoryName) {
        categoryItem.findBy(text(categoryName)).shouldNotHave(exist);
        return this;
    }

    public ProfilePage checkArchiveButtonCategory(String categoryName) {
        $x("//span[contains(text(), '" + categoryName + "')]/ancestor::div[contains(@class, 'MuiGrid-root MuiGrid-item')]//button[@aria-label='Archive category']").shouldBe(visible);
        return this;
    }

    public ProfilePage checkUnarchiveButtonCategory(String categoryName) {
        $x("//span[contains(text(), '" + categoryName + "')]/ancestor::div[contains(@class, 'MuiGrid-root MuiGrid-item')]//button[@aria-label='Unarchive category']").shouldBe(visible);
        return this;
    }


    public ProfilePage clickUnarchiveCategoryButton(String categoryName) {
        $x("//span[contains(text(), '" + categoryName + "')]/ancestor::div[contains(@class, 'MuiGrid-root MuiGrid-item')]//button[@aria-label='Unarchive category']").click();
        return this;
    }


}