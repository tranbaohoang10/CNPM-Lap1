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

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By loginForm = By.cssSelector("form[action$='/login']");
    private By emailInput = By.id("email");
    private By passwordInput = By.id("password");
    private By loginButton = By.cssSelector("button[type='submit']");
    private By loginFeedback = By.cssSelector(".tb_loi, .tb_loi .tb-error, .tb_loi .tb-warning, .tb_loi .tb-success");

    public LoginPage(WebDriver driver) {
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

    public void open() {
        driver.get("http://localhost:8080/mythuat_shop_war_exploded/login");
        delay2Seconds();
    }

    public void login(String email, String password) {
        WebElement form = driver.findElement(loginForm);

        form.findElement(emailInput).clear();
        form.findElement(emailInput).sendKeys(email);
        delay2Seconds();

        form.findElement(passwordInput).clear();
        form.findElement(passwordInput).sendKeys(password);
        delay2Seconds();

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(form.findElement(loginButton)));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'}); arguments[0].click();",
                submitButton
        );
        delay2Seconds();
    }

    public boolean isAt() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginForm));
        delay2Seconds();
        return driver.getCurrentUrl().contains("/login")
                && driver.findElement(loginForm).isDisplayed();
    }

    public boolean staysOnLoginPage() {
        wait.until(d -> d.getCurrentUrl() != null);
        delay2Seconds();
        return driver.getCurrentUrl().contains("/login")
                && !driver.findElements(loginForm).isEmpty();
    }

    public String getLoginFeedbackMessage() {
        wait.until(d -> d.getCurrentUrl() != null);
        delay2Seconds();

        List<String> messages = driver.findElements(loginFeedback)
                .stream()
                .map(WebElement::getText)
                .map(String::trim)
                .filter(text -> !text.isEmpty())
                .collect(Collectors.toList());

        return String.join(" ", messages);
    }
}
