package tests;

import lib.CoreTestCase;
import lib.Platform;
import lib.ui.*;
import lib.ui.factories.ArticlePageObjectFactory;
import lib.ui.factories.MyListPageObjectFactory;
import lib.ui.factories.NavigationUIFactory;
import lib.ui.factories.SearchPageObjectFactory;
import org.junit.Assert;
import org.junit.Test;


public class MyListsTests extends CoreTestCase
{
    private static final String name_of_folder = "Mobil device company";
    private static final String
    login = "Andgoncharov",
    password = "16121983Bad";

    @Test
    public void testSaveFirstArticleToMyList() throws InterruptedException {
        SearchPageObject SearchPageObject = SearchPageObjectFactory.get(driver);
        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Nokia");
        SearchPageObject.clickByArticleWithSubstring("innish");

        ArticlePageObject ArticlePageObject = ArticlePageObjectFactory.get(driver);
        ArticlePageObject.waitForTitleElement();
        String article_title = ArticlePageObject.getArticleTitle();
        if(Platform.getInstance().isAndroid()) {
            ArticlePageObject.addArticleToMyList(name_of_folder);
        } else {ArticlePageObject.addArticlesToMySaved(); }

        if (Platform.getInstance().isMW()) {
            AuthorizationPageObject Auth = new AuthorizationPageObject(driver);
            Auth.clickAuthButton();
            Auth.enterLoginData(login, password);
            Auth.submitForm();
            ArticlePageObject.waitForTitleElement();
            Assert.assertEquals("We are not on the same page after login.",
                    article_title,
                    ArticlePageObject.getArticleTitle());
            ArticlePageObject.addArticlesToMySaved();

        }
        ArticlePageObject.closeArticle();

        NavigationUI NavigationUI = NavigationUIFactory.get(driver);
        NavigationUI.openNavigation();
        NavigationUI.clickMyLists();

        MyListPageObject MyListPageObject = MyListPageObjectFactory.get(driver);
        if (Platform.getInstance().isAndroid())
        { MyListPageObject.openFolderByName(name_of_folder); }
        MyListPageObject.swipeByArticleToDelete(article_title);

    }

    //Refactoring Ex17
    //Другой способ верификации состоит в том, что вновь осушествляем поиск стать
    //и в результатах поиска ищем статью с выделеной звездочкой (добавленую в избранное).

    @Test
    public void testSaveSecondArticleToMyList() throws InterruptedException {
        SearchPageObject SearchPageObject = SearchPageObjectFactory.get(driver);
        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Nokia");

        if (Platform.getInstance().isAndroid()){
            SearchPageObject.clickByArticleWithSubstring("innish technology and telecommunications company");
        } else { SearchPageObject.clickByArticleWithSubstring("innish"); }

        ArticlePageObject ArticlePageObject = ArticlePageObjectFactory.get(driver);
        ArticlePageObject.waitForTitleElement();
        String article_title1 = "HTC";
        String article_title = ArticlePageObject.getArticleTitle();
        if(Platform.getInstance().isAndroid()) {
            ArticlePageObject.addArticleToMyList(name_of_folder);
        } else { ArticlePageObject.addArticlesToMySaved(); }

        if (Platform.getInstance().isMW()) {
            AuthorizationPageObject Auth = new AuthorizationPageObject(driver);
            Auth.clickAuthButton();
            Auth.enterLoginData(login, password);
            Auth.submitForm();
            ArticlePageObject.waitForTitleElement();
            Assert.assertEquals("We are not on the same page after login.",
                    article_title,
                    ArticlePageObject.getArticleTitle());
            ArticlePageObject.addArticlesToMySaved();
        }

        if (Platform.getInstance().isMW()) { SearchPageObject.initSearchInput(); }
        else { ArticlePageObject.closeArticleAndClearSearchLine(); }

        SearchPageObject.typeSearchLine("HTC");
        if(Platform.getInstance().isAndroid()){
            SearchPageObject.clickByArticleWithSubstring("aiwanese electronics company");
        } else { SearchPageObject.clickByArticleWithSubstring("aiwanese"); }

        ArticlePageObject.waitForTitleElement1();

        if(Platform.getInstance().isAndroid()) {
        ArticlePageObject.addArticleMyList();
        ArticlePageObject.savedArticleToMyList(name_of_folder);
        } else { ArticlePageObject.addArticlesToMySavedNotInformationWindow(); }

        ArticlePageObject.closeArticle();

        NavigationUI NavigationUI = NavigationUIFactory.get(driver);
        NavigationUI.openNavigation();
        NavigationUI.clickMyLists();

        MyListPageObject MyListPageObject = MyListPageObjectFactory.get(driver);
        if(Platform.getInstance().isAndroid())
        { MyListPageObject.openFolderByName(name_of_folder); }
        MyListPageObject.swipeByArticleToDelete(article_title1);
        //другой способ верификации оставшейся статьи
        MyListPageObject.waitForArticleNotUsedTitle("Nokia");
    }

}
