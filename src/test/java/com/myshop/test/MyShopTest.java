package com.myshop.test;

import com.myshop.pages.CartPage;
import com.myshop.pages.CheckoutPage;
import com.myshop.pages.HomePage;
import com.myshop.pages.LoginPage;
import com.myshop.pages.ProductDetailsPage;
import com.myshop.pages.SearchResultsPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MyShopTest {
    private static final String VALID_EMAIL =
            System.getProperty("myshop.email", "gaankteam@gmail.com");
    private static final String VALID_PASSWORD =
            System.getProperty("myshop.password", "Hau!25122005");
    private static final String INVALID_PASSWORD = "SaiMatKhau_123";
    private static final String INVALID_SEARCH_KEYWORD = "khongtimthay_selenium_2026_xyz";
    private static final String[] SEARCH_KEYWORDS_WITH_RESULTS = {"TL", "but", "a", "b"};

    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testLoginFailsWithWrongPassword() {
        loginPage.open();
        loginPage.login(VALID_EMAIL, INVALID_PASSWORD);

        Assertions.assertTrue(loginPage.staysOnLoginPage(),
                "Dang nhap sai mat khau nhung van roi khoi trang login");
        Assertions.assertTrue(driver.getCurrentUrl().contains("/login"),
                "Dang nhap sai mat khau nhung URL khong con o trang login");
    }

    @Test
    public void testSearchWithInvalidKeywordShowsNoProducts() {
        loginWithValidCredentials();

        SearchResultsPage searchResultsPage = homePage.searchByKeyword(INVALID_SEARCH_KEYWORD);

        Assertions.assertTrue(searchResultsPage.isAt(),
                "Khong vao duoc trang ket qua tim kiem");
        Assertions.assertFalse(searchResultsPage.hasResults(),
                "Tu khoa khong hop le nhung van tra ve san pham");
    }

    @Test
    public void testLoginSearchProductDetailsAddToCartCheckoutAndPayCOD() {
        loginWithValidCredentials();

        SearchResultsPage searchResultsPage = searchForKeywordWithResults();

        Assertions.assertTrue(searchResultsPage.isAt(),
                "Khong vao duoc trang ket qua tim kiem");
        Assertions.assertTrue(searchResultsPage.hasResults(),
                "Khong tim thay san pham nao tu cac tu khoa tim kiem da chuan bi");

        ProductDetailsPage productDetailsPage = searchResultsPage.openFirstProductDetails();

        Assertions.assertTrue(productDetailsPage.isAt(), "Khong vao duoc trang chi tiet san pham");
        Assertions.assertFalse(productDetailsPage.getProductTitle().isEmpty(), "Ten san pham dang bi rong");
        Assertions.assertTrue(productDetailsPage.isAddToCartButtonDisplayed(), "Khong thay nut them vao gio hang");

        System.out.println("Da vao chi tiet san pham: " + productDetailsPage.getProductTitle());

        productDetailsPage.waitOnDetailsPage();
        productDetailsPage.addToCart();

        CartPage cartPage = productDetailsPage.goToCart();

        Assertions.assertTrue(cartPage.isAt(), "Khong vao duoc trang gio hang");
        Assertions.assertTrue(cartPage.hasItems(), "Gio hang chua co san pham");

        System.out.println("Da vao gio hang");

        CheckoutPage checkoutPage = cartPage.clickPlaceOrder();

        Assertions.assertTrue(checkoutPage.isAt(), "Khong vao duoc trang dat hang / checkout");
        System.out.println("Da vao trang dat hang");

        checkoutPage.fillFixedShippingInfo();
        System.out.println("Da chon cung Binh Thuan - Tuy Phong - Phan Ri Cua");

        checkoutPage.chooseCashOnDelivery();
        System.out.println("Da chon thanh toan khi nhan hang");

        checkoutPage.clickPay();
        System.out.println("Da bam thanh toan");
    }

    private void loginWithValidCredentials() {
        loginPage.open();
        loginPage.login(VALID_EMAIL, VALID_PASSWORD);

        wait.until(d -> d.getCurrentUrl() != null);

        if (driver.getCurrentUrl().contains("/login")) {
            String feedback = loginPage.getLoginFeedbackMessage();
            Assertions.fail("Dang nhap that bai nen khong the toi buoc tim kiem. Thong bao: "
                    + (feedback.isEmpty() ? "(khong co thong bao loi tren giao dien)" : feedback));
        }
    }

    private SearchResultsPage searchForKeywordWithResults() {
        for (String keyword : SEARCH_KEYWORDS_WITH_RESULTS) {
            SearchResultsPage searchResultsPage = homePage.searchByKeyword(keyword);
            if (searchResultsPage.hasResults()) {
                System.out.println("Tim thay san pham voi tu khoa: " + keyword);
                return searchResultsPage;
            }
        }

        Assertions.fail("Khong tim thay san pham nao tu bo tu khoa da khai bao");
        return null;
    }
}
