package ru.geekbrains.testUi;


import io.cucumber.java.en.Given;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

@CommonsLog
@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"json:target/cucumber.json"},
        features = {"classpath:features"},
        glue = {"ru.geekbrains"},
        snippets = CucumberOptions.SnippetType.CAMELCASE)
public class LaunchTest {
}
