package managers;

import pages.BasketPage;
import pages.HomePage;
import pages.SearchResultPage;

/**
 * @author Sergey Nesterov
 */
public class PagesManager {
    private static PagesManager pagesManager;

    public static PagesManager getInstance() {
        if (pagesManager == null) pagesManager = new PagesManager();
        return pagesManager;
    }

    public static void deleteInstance() {
        pagesManager = null;
    }

    private HomePage homePage;
    private SearchResultPage searchResultPage;
    private BasketPage basketPage;

    private PagesManager() {
    }

    public SearchResultPage getSearchResultPage() {
        if (searchResultPage == null) searchResultPage = new SearchResultPage();
        return searchResultPage;
    }

    public HomePage getHomePage() {
        if (homePage == null) homePage = new HomePage();
        return homePage;
    }

    public BasketPage getBasketPage() {
        if (basketPage == null) basketPage = new BasketPage();
        return basketPage;
    }
}