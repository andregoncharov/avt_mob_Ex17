package lib.ui;

import lib.Platform;
import org.junit.Assert;
import org.openqa.selenium.remote.RemoteWebDriver;

abstract public class MyListPageObject extends MainPageObject{

   protected static String
    FOLDER_BY_NAME_TPL,
    SEARCH_INPUT_FOR_MY_LIST,
    SAVED_ARTICLE,
    ARTICLE_BY_TITLE_TPL,
    BUTTON_FILTER_MY_LIST,
    REMOVE_FROM_SAVED_BUTTON,
    SEARCH_INIT_ELEMENT,
    SEARCH_INPUT,
    OPTIONS_REMOVE_FROM_MY_LIST_BUTTON;

    private static String getFolderXpathByName(String name_of_folder)
    {
        return FOLDER_BY_NAME_TPL.replace("{FOLDER_NAME}",name_of_folder);
    }

    private static String getSavedArticleXpathByTitle(String article_title)
    {
        return ARTICLE_BY_TITLE_TPL.replace("{TITLE}",article_title);
    }
    private static String getSavedArticleXpathByTitle1(String article_title1)
    {
        return ARTICLE_BY_TITLE_TPL.replace("{TITLE}",article_title1);
    }

    private static String getRemoveButtonByTitle(String article_title)
    {
        return REMOVE_FROM_SAVED_BUTTON.replace("{TITLE}",article_title);
    }

    public MyListPageObject(RemoteWebDriver driver)
    {
        super(driver);
    }

    public void openFolderByName(String name_of_folder)
    {
        String folder_name_xpath = getFolderXpathByName(name_of_folder);

        this.waitForElementAndClick(
                folder_name_xpath,
                "Cannot find folder by name " + name_of_folder,
                10
        );
    }

    public void waitForArticleToAppearByTitle(String article_title)
    {
        String article_xpath = getSavedArticleXpathByTitle(article_title);
        this.waitForElementPresent(article_xpath, "Cannot find saved article by title " + article_title, 15);
    }

    public void waitForArticleToMyListAndClick(String article_title1)
    {
        String article_xpath = getSavedArticleXpathByTitle1(article_title1);
        this.waitForElementPresent(article_xpath, "Cannot find saved article by title " + article_title1, 15);
        this.waitForElementAndClick(article_xpath, "Cannot find article and click", 10);
    }

    public void waitForArticleToDisappearByTitle(String article_title)
    {
        String article_xpath = getSavedArticleXpathByTitle(article_title);
        this.waitForElementNotPresent(article_xpath, "Saved article still present with title " + article_title, 15);
    }

    public void swipeByArticleToDelete(String article_title) throws InterruptedException {
        this.waitForArticleToAppearByTitle(article_title);
        String article_xpath = getSavedArticleXpathByTitle(article_title);
        if (Platform.getInstance().isIOS() || Platform.getInstance().isAndroid()){
        this.swipeElementToLeft(article_xpath, "Cannot find saved article");
    } else {
            String remove_locator = getRemoveButtonByTitle(article_title);
            this.waitForElementAndClick(remove_locator,"Cannot click button to remove article from saved.",5);
        }


        if(Platform.getInstance().isIOS()){
            this.clickElementToTheRightUpperCorner(article_xpath, "Cannot find saved article");
        }

        if(Platform.getInstance().isMW()) {
            driver.navigate().refresh();
        }

        this.waitForArticleToDisappearByTitle(article_title);
    }

    //Метод который осуществляет поиск в листе закладок и считает количество 'ариклов' после свайпа одного из них.
    //А для MW осуществляем поиск статьи со звездочкой
    public void waitForArticleNotUsedTitle(String name_of_search) throws InterruptedException {
        if(Platform.getInstance().isMW()){
            this.waitForElementAndClick(SEARCH_INIT_ELEMENT,"Cannot find and click search init element",5);
            this.waitForElementPresent(SEARCH_INPUT,"Cannot find search input after clicking search init element",5);
            this.waitForElementAndSendKeys(SEARCH_INPUT, name_of_search,"Cannot find adn type into search input",5);
            this.waitForElementPresent(OPTIONS_REMOVE_FROM_MY_LIST_BUTTON, "Cannot find button to add an article to saved list after removing it from this list before", 10);
        }
        if(Platform.getInstance().isIOS()) {
            this.waitForElementAndSendKeys(SEARCH_INPUT_FOR_MY_LIST, name_of_search, "Cannot find and type into search input", 5);
            this.waitForElementPresent(SAVED_ARTICLE, "Cannot find anything by the request " + SAVED_ARTICLE, 15);
            int amount_of_saved_results = getAAmountOfElements(SAVED_ARTICLE);
            Assert.assertTrue("We found too few results", amount_of_saved_results == 1);
        }
        if (Platform.getInstance().isAndroid()) {
            this.waitForElementAndClick(BUTTON_FILTER_MY_LIST, "Cannot find and click Filter my list",5);
            this.waitForElementAndSendKeys(SEARCH_INPUT_FOR_MY_LIST, name_of_search, "Cannot find and type into search input", 10);
            this.waitForElementPresent(SAVED_ARTICLE, "Cannot find anything by the request " + SAVED_ARTICLE, 15);
            int amount_of_saved_results = getAAmountOfElements(SAVED_ARTICLE);
            Assert.assertTrue("We found too few results", amount_of_saved_results == 1);
        }
    }

}
