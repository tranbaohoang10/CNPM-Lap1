package com.myshop.test;

import com.myshop.pages.CartPage;
import com.myshop.pages.HomePage;
import com.myshop.pages.LoginPage;
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
    public void testLoginAddRandomProductAndGoToCart() {
        loginPage.open();
        loginPage.login("tranbaohoang1105@gmail.com", "123");

        wait.until(d -> d.getCurrentUrl() != null && !d.getCurrentUrl().contains("/login"));

        homePage.clickRandomAddToCart();

        CartPage cartPage = homePage.goToCart();

        Assertions.assertTrue(cartPage.isAt(), "Không vào được trang giỏ hàng");
        Assertions.assertTrue(cartPage.hasItems(), "Giỏ hàng chưa có sản phẩm");
    }
}