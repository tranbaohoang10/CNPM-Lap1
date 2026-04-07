package com.myshop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage {
    private WebDriver driver;

    private By cartTitle = By.xpath("//h2[contains(normalize-space(),'Giỏ hàng của bạn')]");
    private By cartItems = By.cssSelector(".cart-item-card");

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isAt() {
        return driver.getCurrentUrl().contains("/cart")
                && !driver.findElements(cartTitle).isEmpty();
    }

    public boolean hasItems() {
        return !driver.findElements(cartItems).isEmpty();
    }
}