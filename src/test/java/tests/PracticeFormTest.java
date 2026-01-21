package tests;

import data.UsersDataProvider;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.PracticeFormPage;
import utils.TestBase;

import java.util.List;

public class PracticeFormTest extends TestBase {

    /**
     * Positive: Fill form with valid data and verify success modal + a few fields.
     */
    @Test(description = "Positive: valid submission shows success modal",
            dataProvider = "validUsers", dataProviderClass = UsersDataProvider.class)
    public void testPositiveSubmission(String firstName, String lastName, String email,
                                       String gender, String mobile, String dob,
                                       List<String> subjects, List<String> hobbies,
                                       String address, String state, String city) {

        PracticeFormPage page = new PracticeFormPage(driver);

        page.setFirstName(firstName);
        page.setLastName(lastName);
        page.setEmail(email);
        page.selectGender(gender);
        page.setMobile(mobile);
        page.setDOB(dob);
        page.addSubjects(subjects);
        page.selectHobbies(hobbies);
        page.uploadPicture(config.getProperty("uploadFilePath"));
        page.setCurrentAddress(address);
        page.selectStateAndCity(state, city);
        page.submit();

        Assert.assertTrue(page.isSuccessModalVisible(), "Success modal should be visible");
        Assert.assertEquals(page.getModalTitle(), "Thanks for submitting the form");
        Assert.assertEquals(page.getSubmittedValue("Student Name"), firstName + " " + lastName);
        Assert.assertEquals(page.getSubmittedValue("Student Email"), email);
        Assert.assertEquals(page.getSubmittedValue("Gender"), gender);
    }

    /**
     * Negative: Missing Mobile field should not submit successfully.
     */
    @Test(description = "Negative: missing Mobile shows validation and no success modal")
    public void testNegative_MissingMobile() {

        PracticeFormPage page = new PracticeFormPage(driver);

        page.setFirstName("Jane");
        page.setLastName("Doe");
        page.setEmail("jane.doe@example.com");
        page.selectGender("Female");
        // Skipping mobile
        page.setDOB("01 Feb 1997");
        page.addSubjects(List.of("English"));
        page.selectHobbies(List.of("Music"));
        page.uploadPicture(config.getProperty("uploadFilePath"));
        page.setCurrentAddress("T Nagar, Chennai");
        page.selectStateAndCity(config.getProperty("state"), config.getProperty("city"));
        page.submit();

        // 1. Modal should NOT appear
        Assert.assertFalse(page.isSuccessModalVisible(),
                "Success modal should NOT appear when mobile is missing");

        // 2. Mobile field should remain empty (simple reliable validation)
        Assert.assertTrue(page.isMobileFieldEmpty(),
                "Mobile field is required; it should remain empty and prevent submission");
    }


    /**
     * Data-driven: Try different genders and hobbies quickly.
     */
    @Test(description = "Data-driven: submit with different genders & hobbies",
            dataProvider = "genderHobbyMatrix", dataProviderClass = UsersDataProvider.class)
    public void testDataDriven_GenderHobby(String gender, List<String> hobbies) {

        PracticeFormPage page = new PracticeFormPage(driver);

        page.setFirstName("Alex");
        page.setLastName("Roy");
        page.setEmail("alex.roy@example.com");
        page.selectGender(gender);
        page.setMobile("9998887776");
        page.setDOB("15 Mar 1992");
        page.addSubjects(List.of("Maths"));
        page.selectHobbies(hobbies);
        page.uploadPicture(config.getProperty("uploadFilePath"));
        page.setCurrentAddress("Plot 7, OMR, Chennai");
        page.selectStateAndCity(config.getProperty("state"), config.getProperty("city"));
        page.submit();

        Assert.assertTrue(page.isSuccessModalVisible(), "Success modal should be visible");
        Assert.assertEquals(page.getSubmittedValue("Gender"), gender);
        Assert.assertTrue(page.getSubmittedValue("Hobbies").contains(hobbies.get(0)),
                "Hobbies in the table should contain at least the first selected hobby");
    }
}