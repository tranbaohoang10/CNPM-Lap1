package com.myshop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CheckoutPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By checkoutForm = By.id("checkoutForm");

    private By provinceSelect = By.id("provinceSelect");
    private By districtSelect = By.id("districtSelect");
    private By wardSelect = By.id("wardSelect");
    private By addressInput = By.id("address");

    private By codOption = By.cssSelector("input[type='radio'][name='payment'][value='COD']");

    private By payButton = By.xpath(
            "//button[contains(normalize-space(),'Thanh toán') or contains(normalize-space(),'Đặt hàng')] | " +
                    "//input[@type='submit' and (contains(@value,'Thanh toán') or contains(@value,'Đặt hàng'))]"
    );

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(40));
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
        return !driver.findElements(checkoutForm).isEmpty()
                && driver.getCurrentUrl().contains("/checkout");
    }

    private void triggerChange(WebElement element, String value) {
        ((JavascriptExecutor) driver).executeScript(
                "const el = arguments[0];" +
                        "const value = arguments[1];" +
                        "el.value = value;" +
                        "el.dispatchEvent(new Event('input', { bubbles: true }));" +
                        "el.dispatchEvent(new Event('change', { bubbles: true }));",
                element, value
        );
    }

    private WebElement waitUntilHasRealOptions(By selectLocator) {
        return wait.until(d -> {
            WebElement el = d.findElement(selectLocator);
            Select select = new Select(el);

            for (WebElement option : select.getOptions()) {
                String text = option.getText() == null ? "" : option.getText().trim();
                String value = option.getAttribute("value") == null ? "" : option.getAttribute("value").trim();

                boolean placeholder =
                        text.isEmpty()
                                || text.startsWith("--")
                                || value.isEmpty()
                                || value.equals("0")
                                || value.equals("-1");

                if (!placeholder) {
                    return el;
                }
            }
            return null;
        });
    }

    public void selectProvinceByText(String provinceText) {
        WebElement provinceElement = wait.until(d -> d.findElement(provinceSelect));

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", provinceElement
        );
        delay2Seconds();

        Select province = new Select(provinceElement);
        province.selectByVisibleText(provinceText);

        String provinceValue = province.getFirstSelectedOption().getAttribute("value");
        System.out.println("Đã chọn tỉnh: " + province.getFirstSelectedOption().getText()
                + " | value = " + provinceValue);

        triggerChange(provinceElement, provinceValue);

        ((JavascriptExecutor) driver).executeScript(
                "if (typeof window.loadDistricts === 'function') { window.loadDistricts(arguments[0]); }",
                provinceValue
        );

        delay2Seconds();
        waitUntilHasRealOptions(districtSelect);
        delay2Seconds();
    }

    public void selectDistrictByText(String districtText) {
        WebElement districtElement = waitUntilHasRealOptions(districtSelect);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", districtElement
        );
        delay2Seconds();

        Select district = new Select(districtElement);
        district.selectByVisibleText(districtText);

        String districtValue = district.getFirstSelectedOption().getAttribute("value");
        System.out.println("Đã chọn huyện: " + district.getFirstSelectedOption().getText()
                + " | value = " + districtValue);

        triggerChange(districtElement, districtValue);

        ((JavascriptExecutor) driver).executeScript(
                "if (typeof window.loadWards === 'function') { window.loadWards(arguments[0]); }",
                districtValue
        );

        delay2Seconds();
        waitUntilHasRealOptions(wardSelect);
        delay2Seconds();
    }

    public void selectWardByText(String wardText) {
        WebElement wardElement = waitUntilHasRealOptions(wardSelect);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", wardElement
        );
        delay2Seconds();

        Select ward = new Select(wardElement);
        ward.selectByVisibleText(wardText);

        System.out.println("Đã chọn phường/xã: " + ward.getFirstSelectedOption().getText());
        delay2Seconds();
    }

    public void keepCurrentAddress() {
        WebElement input = wait.until(d -> d.findElement(addressInput));
        String currentAddress = input.getAttribute("value");
        System.out.println("Giữ nguyên địa chỉ: " + currentAddress);
        delay2Seconds();
    }

    public void chooseCashOnDelivery() {
        WebElement cod = wait.until(d -> d.findElement(codOption));

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", cod
        );
        delay2Seconds();

        if (!cod.isSelected()) {
            cod.click();
        }

        delay2Seconds();
    }

    public void clickPay() {
        WebElement button = wait.until(d -> {
            List<WebElement> buttons = d.findElements(payButton);
            for (WebElement btn : buttons) {
                if (btn.isDisplayed() && btn.isEnabled()) {
                    return btn;
                }
            }
            return null;
        });

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", button
        );
        delay2Seconds();

        button.click();
        delay2Seconds();
    }

    public void fillFixedShippingInfo() {
        selectProvinceByText("Bình Thuận");
        selectDistrictByText("Huyện Tuy Phong");
        selectWardByText("Thị trấn Phan Rí Cửa");
        keepCurrentAddress();
    }
}