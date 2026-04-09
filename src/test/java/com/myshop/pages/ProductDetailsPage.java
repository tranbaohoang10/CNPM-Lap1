package com.myshop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductDetailsPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By productTitle = By.cssSelector("h1.product-title");
    private By addToCartButton = By.id("addToCartBtn");
    private By cartIcon = By.id("cartIcon");

    public ProductDetailsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private void delay2Seconds() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean isAt() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(productTitle));
        delay2Seconds();
        return driver.getCurrentUrl().contains("/DetailsProductController")
                && driver.findElement(productTitle).isDisplayed();
    }

    public String getProductTitle() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(productTitle));
        delay2Seconds();
        return driver.findElement(productTitle).getText().trim();
    }

    public boolean isAddToCartButtonDisplayed() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(addToCartButton));
        delay2Seconds();
        return driver.findElement(addToCartButton).isDisplayed();
    }

    public void waitOnDetailsPage() {
        delay2Seconds();
    }

    public void addToCart() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", button
        );
        delay2Seconds();

        button.click();
        delay2Seconds();
    }

    public CartPage goToCart() {
        WebElement cart = wait.until(ExpectedConditions.elementToBeClickable(cartIcon));

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", cart
        );
        delay2Seconds();

        cart.click();

        wait.until(d -> d.getCurrentUrl() != null && d.getCurrentUrl().contains("/cart"));
        delay2Seconds();

        return new CartPage(driver);
    }
}