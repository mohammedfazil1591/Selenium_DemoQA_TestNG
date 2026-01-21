package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    public static void initDriver(String browser) {
        if (tlDriver.get() == null) {
            // Only Chrome for now (beginner-friendly)
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            tlDriver.set(new ChromeDriver(options)); // Selenium Manager handles driver
        }
    }

    public static WebDriver getDriver() {
        return tlDriver.get();
    }

    public static void quitDriver() {
        if (tlDriver.get() != null) {
            tlDriver.get().quit();
            tlDriver.remove();
        }
    }
}