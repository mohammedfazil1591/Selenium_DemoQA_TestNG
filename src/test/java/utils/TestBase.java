package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class TestBase {

    protected WebDriver driver;
    protected Properties config = new Properties();

    @BeforeSuite(alwaysRun = true)
    public void loadConfig() throws IOException {
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            config.load(fis);
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverFactory.initDriver(config.getProperty("browser", "chrome"));
        driver = DriverFactory.getDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0)); // keep it simple; rely on direct locators
        driver.get(config.getProperty("baseUrl"));

        // Remove footer/ads that may block clicks (basic)
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("document.getElementById('fixedban')?.remove();");
            js.executeScript("document.querySelector('footer')?.remove();");
        } catch (Exception ignored) { }

        // Stronger cleanup for ad overlays (Ad.Plus / Google Ads iframes/anchors)
        removeAdOverlays();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    /**
     * Removes common ad/anchor overlays on DemoQA pages that can intercept clicks.
     */
    private void removeAdOverlays() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(
                    "document.querySelectorAll("
                            + "  '#fixedban, footer,"
                            + "   iframe[id^=\"google_ads_iframe\"], iframe[name^=\"google_ads_iframe\"],"
                            + "   div[id*=\"Ad.Plus-Anchor\"], div[id*=\"adplus\"], #adplus-anchor'"
                            + ").forEach(e => e.remove());"
            );
        } catch (Exception ignored) { }
    }
}