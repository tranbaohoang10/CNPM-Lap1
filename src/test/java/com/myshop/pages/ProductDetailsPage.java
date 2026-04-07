package com.myshop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductDetailsPage {
    private WebDriver driver;

    private By addToCartButton = By.id("addToCartBtn");

    public ProductDetailsPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickAddToCart() {
        driver.findElement(addToCartButton).click();
    }
}