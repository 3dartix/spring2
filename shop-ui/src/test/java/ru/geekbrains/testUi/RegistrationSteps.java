package ru.geekbrains.testUi;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class RegistrationSteps {

    private WebDriver webDriver;

    @Given("^I get driver$")
    public void initDriver() {
        new DriverInitializer();
    }

    @Given("^I open web browser$")
    public void iOpenBrowser(){
        webDriver = DriverInitializer.getWebDriver();
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:8080/");
    }

    @When("I click button registration")
    public void iClickBtnRegistration() throws Throwable {
        Thread.sleep(1000);
        WebElement webElement = webDriver.findElement(By.id("btn_registation"));
        webElement.click();
    }

    @When("I fill out the form: username = {string} and password = {string} and firstName = {string} and lastName = {string} and phone = {string} and email = {string}")
    public void iFillOutForm(String username, String rPassword,
                             String firstName, String lastName, String phone,
                             String email

    ) throws Throwable {
        Thread.sleep(1000);
        WebElement webElement = webDriver.findElement(By.id("username"));
        webElement.clear();
        webElement.sendKeys(username);

        webElement = webDriver.findElement(By.id("password"));
        webElement.clear();
        webElement.sendKeys(rPassword);

        webElement = webDriver.findElement(By.id("confirmPassword"));
        webElement.clear();
        webElement.sendKeys(rPassword);

        webElement = webDriver.findElement(By.id("firstName"));
        webElement.clear();
        webElement.sendKeys(firstName);

        webElement = webDriver.findElement(By.id("lastName"));
        webElement.clear();
        webElement.sendKeys(lastName);

        webElement = webDriver.findElement(By.id("phone"));
        webElement.clear();
        webElement.sendKeys(phone);

        webElement = webDriver.findElement(By.id("email"));
        webElement.clear();
        webElement.sendKeys(email);

        webElement = webDriver.findElement(By.id("btn-registr-ok"));
        webElement.click();
    }

    @When("I delete test user")
    public void iDeleteTestUser() throws Throwable {
        webDriver.get("http://localhost:8080/user/delete/test-user");
    }

//    delete/test-user I delete test user
}
