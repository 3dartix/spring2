package ru.geekbrains.testUi;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.test.context.TestPropertySource;

import java.util.Properties;

public class DriverInitializer {

    private static Properties properties = null;
    private static WebDriver webDriver;

    public static WebDriver getWebDriver() {
        return webDriver;
    }

    static {
        try {
            properties = new Properties();
            properties.setProperty("port", "http://localhost:8080/");
            properties.setProperty("browser", "chrome");
//            properties.load(DriverInitializer.class.getClassLoader()
//                    .getResourceAsStream("application-test.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DriverInitializer() {
        webDriver = getDriver();
    }

    private WebDriver getDriver() {
        switch (getProperty("browser")) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver();
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver();
            default:
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver();
        }
    }

    public static String getProperty(String key) {
        return properties == null ? null : properties.getProperty(key, "");
    }
}
