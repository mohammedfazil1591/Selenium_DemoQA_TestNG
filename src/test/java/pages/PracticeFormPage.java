package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.nio.file.Paths;
import java.util.List;

public class PracticeFormPage {

    private final WebDriver driver;

    public PracticeFormPage(WebDriver driver) {
        this.driver = driver;
    }

    // --- small helpers to make clicks reliable ---
    private void scrollToCenter(WebElement el) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", el);
    }

    private void jsClick(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    private void removeAds() {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "document.querySelectorAll("
                            + "  '#fixedban, footer,"
                            + "   iframe[id^=\"google_ads_iframe\"], iframe[name^=\"google_ads_iframe\"],"
                            + "   div[id*=\"Ad.Plus-Anchor\"], div[id*=\"adplus\"], #adplus-anchor'"
                            + ").forEach(e => e.remove());"
            );
        } catch (Exception ignored) { }
    }

    // === Basic actions ===

    public void setFirstName(String value) {
        driver.findElement(By.id("firstName")).clear();
        driver.findElement(By.id("firstName")).sendKeys(value);
    }

    public void setLastName(String value) {
        driver.findElement(By.id("lastName")).clear();
        driver.findElement(By.id("lastName")).sendKeys(value);
    }

    public void setEmail(String value) {
        driver.findElement(By.id("userEmail")).clear();
        driver.findElement(By.id("userEmail")).sendKeys(value);
    }

    public void selectGender(String genderText) {
        removeAds(); // extra safety before clicking radios
        WebElement label = driver.findElement(By.xpath("//label[text()='" + genderText + "']"));
        scrollToCenter(label);
        try {
            label.click();
        } catch (ElementClickInterceptedException e) {
            jsClick(label); // fallback if an overlay blocks the native click
        }
    }

    public void setMobile(String value) {
        driver.findElement(By.id("userNumber")).clear();
        driver.findElement(By.id("userNumber")).sendKeys(value);
    }

    /** Example input: "20 Jan 1995" */
    public void setDOB(String dobFormatted) {
        WebElement dob = driver.findElement(By.id("dateOfBirthInput"));
        dob.click();
        dob.sendKeys(Keys.chord(Keys.CONTROL, "a")); // select all
        dob.sendKeys(dobFormatted);
        dob.sendKeys(Keys.ENTER);
    }

    public void addSubjects(List<String> subjects) {
        WebElement input = driver.findElement(By.id("subjectsInput"));
        for (String s : subjects) {
            input.sendKeys(s);
            input.sendKeys(Keys.ENTER);
        }
    }

    public void selectHobbies(List<String> hobbies) {
        for (String h : hobbies) {
            removeAds(); // extra safety before clicking checkboxes
            WebElement label = driver.findElement(By.xpath("//label[text()='" + h + "']"));
            scrollToCenter(label);
            try {
                label.click();
            } catch (ElementClickInterceptedException e) {
                jsClick(label);
            }
        }
    }

    public void uploadPicture(String relativePath) {
        String absolutePath = Paths.get(relativePath).toAbsolutePath().toString();
        driver.findElement(By.id("uploadPicture")).sendKeys(absolutePath);
    }

    public void setCurrentAddress(String value) {
        driver.findElement(By.id("currentAddress")).clear();
        driver.findElement(By.id("currentAddress")).sendKeys(value);
    }

    public void selectStateAndCity(String state, String city) {
        // For react-select controls, click, type, then ENTER
        driver.findElement(By.id("state")).click();
        driver.findElement(By.id("react-select-3-input")).sendKeys(state + Keys.ENTER);

        driver.findElement(By.id("city")).click();
        driver.findElement(By.id("react-select-4-input")).sendKeys(city + Keys.ENTER);
    }

    public void submit() {
        removeAds(); // ensure nothing blocks the submit button
        WebElement btn = driver.findElement(By.id("submit"));
        scrollToCenter(btn);
        try {
            btn.click();
        } catch (ElementClickInterceptedException e) {
            jsClick(btn);
        }
    }

    // === Assertion helpers ===

    public boolean isSuccessModalVisible() {
        try {
            return driver.findElement(By.id("example-modal-sizes-title-lg")).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public String getModalTitle() {
        return driver.findElement(By.id("example-modal-sizes-title-lg")).getText().trim();
    }

    /** Read a value from the result table by its header (e.g., "Student Name") */
    public String getSubmittedValue(String header) {
        By rowLocator = By.xpath("//div[@class='table-responsive']//td[1][text()='" + header + "']/following-sibling::td");
        return driver.findElement(rowLocator).getText().trim();
    }

    /** Basic negative validation: verify the mobile field is still empty */
    public boolean isMobileFieldEmpty() {
        String value = driver.findElement(By.id("userNumber")).getAttribute("value");
        return value == null || value.isEmpty();
    }
}