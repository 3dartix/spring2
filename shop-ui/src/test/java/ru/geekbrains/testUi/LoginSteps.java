package ru.geekbrains.testUi;

//import io.cucumber.java.Before;
//import io.cucumber.java.en.Given;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginSteps {
    private WebDriver webDriver;


    @When("I click button logIn")
    public void iClickBtnlogIn() throws Throwable {
        Thread.sleep(1000);
        webDriver = DriverInitializer.getWebDriver();
        WebElement webElement = webDriver.findElement(By.id("navLoginSubmit"));
        webElement.click();
    }


//    @When("^I provide username as \"([^\"]*)\" and password as \"([^\"]*)\"$")
    @When("I should be told {string} and {string}")
    public void iProvideUsernameAsAndPasswordAs(String username, String password) throws Throwable {
        webDriver.get("http://localhost:8080/");
        WebElement webElement = webDriver.findElement(By.id("navLoginField"));
        webElement.sendKeys(username);
        webElement = webDriver.findElement(By.id("navPasswordField"));
        webElement.sendKeys(password);
        webElement = webDriver.findElement(By.id("navLoginSubmit"));
        webElement.click();
    }

    @When("name should be {string}")
    public void iFindName(String name) throws Throwable {
        Thread.sleep(3000);
        WebElement webElement = webDriver.findElement(By.id("dd_name"));
        assertThat(webElement.getText()).isEqualTo(name);
    }

    @When("I click logout button")
    public void iClickLogOut() throws Throwable {
        Thread.sleep(1000);
        WebElement webElement = webDriver.findElement(By.id("navLogout"));
        webElement.click();
    }

    @When("I find button logIn")
    public void iFindBtnlogIn() throws Throwable {
        Thread.sleep(1000);
        WebElement webElement = webDriver.findElement(By.id("navLoginSubmit"));
    }

    @After
    public void quitBrowser() throws InterruptedException {
        Thread.sleep(2000);
        webDriver.quit();
    }

}
