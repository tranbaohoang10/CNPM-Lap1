package com.myshop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class SearchResultsPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By body = By.tagName("body");
    private By productDetailLinks =
            By.xpath("//a[contains(@href,'/DetailsProductController?id=')]");

    public SearchResultsPage(WebDriver driver) {
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
        wait.until(d -> d.getCurrentUrl() != null && d.getCurrentUrl().contains("/search"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(body));
        delay2Seconds();
        return driver.getCurrentUrl().contains("/search");
    }

    public int getVisibleResultCount() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(body));
        delay2Seconds();
        return driver.findElements(productDetailLinks)
                .stream()
                .filter(WebElement::isDisplayed)
                .collect(Collectors.toList())
                .size();
    }

    public boolean hasResults() {
        return getVisibleResultCount() > 0;
    }

    public ProductDetailsPage openFirstProductDetails() {
        List<WebElement> links = wait.until(d -> {
            List<WebElement> visibleLinks = d.findElements(productDetailLinks)
                    .stream()
                    .filter(WebElement::isDisplayed)
                    .collect(Collectors.toList());
            return visibleLinks.isEmpty() ? null : visibleLinks;
        });

        WebElement firstLink = links.get(0);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", firstLink
        );
        delay2Seconds();

        wait.until(ExpectedConditions.elementToBeClickable(firstLink)).click();
        delay2Seconds();

        return new ProductDetailsPage(driver);
    }
}
