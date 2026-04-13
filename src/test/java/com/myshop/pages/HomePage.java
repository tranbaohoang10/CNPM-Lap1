package com.myshop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

    private By productDetailLinks =
            By.xpath("//a[contains(@href,'/DetailsProductController?id=')]");
    private By searchForm = By.id("headerSearchForm");
    private By searchInput = By.id("headerSearchInput");
    private By searchButton = By.cssSelector(".btn-search");

    public HomePage(WebDriver driver) {
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

    public ProductDetailsPage openRandomProductDetails() {
        List<WebElement> links = wait.until(d -> {
            List<WebElement> list = d.findElements(productDetailLinks)
                    .stream()
                    .filter(WebElement::isDisplayed)
                    .collect(Collectors.toList());
            return list.isEmpty() ? null : list;
        });

        int randomIndex = random.nextInt(links.size());
        WebElement randomLink = links.get(randomIndex);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", randomLink
        );
        delay2Seconds();

        wait.until(ExpectedConditions.elementToBeClickable(randomLink)).click();
        delay2Seconds();

        return new ProductDetailsPage(driver);
    }

    public SearchResultsPage searchByKeyword(String keyword) {
        WebElement form = wait.until(ExpectedConditions.visibilityOfElementLocated(searchForm));
        WebElement input = form.findElement(searchInput);

        input.clear();
        input.sendKeys(keyword);
        delay2Seconds();

        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(form.findElement(searchButton)));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'}); arguments[0].click();",
                button
        );
        wait.until(d -> d.getCurrentUrl() != null && d.getCurrentUrl().contains("/search"));
        delay2Seconds();

        return new SearchResultsPage(driver);
    }
}
