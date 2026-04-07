package com.myshop.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;
    private Random random = new Random();

    private By addToCartButtons =
            By.xpath("//form[contains(@action,'AddToCart')]//button[@type='submit']");

    private By cartIcon = By.id("cartIcon");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickRandomAddToCart() {
        List<WebElement> buttons = wait.until(d -> {
            List<WebElement> list = d.findElements(addToCartButtons)
                    .stream()
                    .filter(WebElement::isDisplayed)
                    .collect(Collectors.toList());
            return list.isEmpty() ? null : list;
        });

        int randomIndex = random.nextInt(buttons.size());
        WebElement randomButton = buttons.get(randomIndex);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", randomButton
        );

        wait.until(ExpectedConditions.elementToBeClickable(randomButton)).click();

        wait.until(d ->
                ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete")
        );

        System.out.println("Đã thêm sản phẩm ngẫu nhiên, vị trí: "
                + (randomIndex + 1) + "/" + buttons.size());
    }

    public CartPage goToCart() {
        WebElement cart = wait.until(ExpectedConditions.elementToBeClickable(cartIcon));

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", cart
        );

        cart.click();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return new CartPage(driver);
    }
}