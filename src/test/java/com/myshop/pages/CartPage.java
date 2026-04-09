package com.myshop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CartPage {
    private WebDriver driver;

    private By cartTitle = By.xpath("//h2[contains(normalize-space(),'Giỏ hàng của bạn')]");
    private By cartItems = By.cssSelector(".cart-item-card");

    // sửa locator này nếu nút đặt hàng bên web bạn khác
    private By placeOrderButton = By.xpath(
            "//a[contains(@href,'/checkout') or contains(normalize-space(),'Đặt hàng') or contains(normalize-space(),'Thanh toán')] | " +
                    "//button[contains(normalize-space(),'Đặt hàng') or contains(normalize-space(),'Thanh toán')]"
    );

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    private void delay2Seconds() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean isAt() {
        delay2Seconds();
        return driver.getCurrentUrl().contains("/cart")
                && !driver.findElements(cartTitle).isEmpty();
    }

    public boolean hasItems() {
        delay2Seconds();
        return !driver.findElements(cartItems).isEmpty();
    }

    public CheckoutPage clickPlaceOrder() {
        WebElement button = driver.findElement(placeOrderButton);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", button
        );
        delay2Seconds();

        button.click();
        delay2Seconds();

        return new CheckoutPage(driver);
    }
}