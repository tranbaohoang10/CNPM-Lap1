package com.myshop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage {
    private WebDriver driver;

    private By loginForm = By.cssSelector("form[action$='/login']");
    private By emailInput = By.id("email");
    private By passwordInput = By.id("password");
    private By loginButton = By.cssSelector("button[type='submit']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get("http://localhost:8080/mythuat_shop_war_exploded/login");
    }

    public void login(String email, String password) {
        WebElement form = driver.findElement(loginForm);

        form.findElement(emailInput).clear();
        form.findElement(emailInput).sendKeys(email);

        form.findElement(passwordInput).clear();
        form.findElement(passwordInput).sendKeys(password);

        form.findElement(loginButton).click();
    }
}