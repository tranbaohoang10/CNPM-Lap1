package com.myshop.test;

import com.myshop.pages.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MyShopTest {

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
    public void testLoginAddToCartCheckoutAndPayCOD() {
        loginPage.open();
        loginPage.login("tranbaohoang1105@gmail.com", "123");

        wait.until(d -> d.getCurrentUrl() != null && !d.getCurrentUrl().contains("/login"));

        ProductDetailsPage productDetailsPage = homePage.openRandomProductDetails();

        Assertions.assertTrue(productDetailsPage.isAt(), "Không vào được trang chi tiết sản phẩm");
        Assertions.assertFalse(productDetailsPage.getProductTitle().isEmpty(), "Tên sản phẩm đang bị rỗng");
        Assertions.assertTrue(productDetailsPage.isAddToCartButtonDisplayed(), "Không thấy nút thêm vào giỏ hàng");

        System.out.println("Đã vào chi tiết sản phẩm: " + productDetailsPage.getProductTitle());

        productDetailsPage.waitOnDetailsPage();
        productDetailsPage.addToCart();

        CartPage cartPage = productDetailsPage.goToCart();

        Assertions.assertTrue(cartPage.isAt(), "Không vào được trang giỏ hàng");
        Assertions.assertTrue(cartPage.hasItems(), "Giỏ hàng chưa có sản phẩm");

        System.out.println("Đã vào giỏ hàng");

        CheckoutPage checkoutPage = cartPage.clickPlaceOrder();

        Assertions.assertTrue(checkoutPage.isAt(), "Không vào được trang đặt hàng / checkout");
        System.out.println("Đã vào trang đặt hàng");

        checkoutPage.fillFixedShippingInfo();
        System.out.println("Đã chọn cứng Bình Thuận - Tuy Phong - Phan Rí Cửa");

        checkoutPage.chooseCashOnDelivery();
        System.out.println("Đã chọn thanh toán khi nhận hàng");

        checkoutPage.clickPay();
        System.out.println("Đã bấm thanh toán");
    }
}