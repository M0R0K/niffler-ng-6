package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;

public class ProfileWebTest {
    private static final Config CFG = Config.getInstance();

    @User(
            name = "test", categories = @Category(
            archived = false
    ))
    @Test
    void categoryIsArchivedSuccessfully(CategoryJson category) {

        final String successMessage = "Category " + category.name() + " is archived";
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("test", "123456")
                .menuButtonClick()
                .profileButtonClick();
        new ProfilePage().clickArchiveButton()
                .clickArchiveButtonSubmit()
                .checkSuccessAlert(successMessage)
                .checkCategoryNotInlist(category.name())
                .clickArchiveSwitcher()
                .checkCategoryInlist(category.name())
                .checkUnarchiveButtonCategory(category.name());
    }


    @User(
            name = "test", categories = @Category(
            archived = true
    ))
    @Test
    void categoryIsUnarchivedSuccessfully(CategoryJson category) {
        final String successMessage = "Category " + category.name() + " is unarchived";
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("test", "123456")
                .menuButtonClick()
                .profileButtonClick();
        new ProfilePage()
                .clickArchiveSwitcher()
                .checkCategoryInlist(category.name())
                .clickUnarchiveCategoryButton(category.name())
                .clickUnarchiveButton()
                .checkSuccessAlert(successMessage)
                .checkArchiveButtonCategory(category.name());
    }
}
