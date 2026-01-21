package data;

import org.testng.annotations.DataProvider;

import java.util.List;

public class UsersDataProvider {

    @DataProvider(name = "validUsers")
    public Object[][] validUsers() {
        return new Object[][]{
                // firstName, lastName, email, gender, mobile, dob, subjects, hobbies, address, state, city
                {
                        "John", "Doe", "john.doe@example.com", "Male", "9876543210",
                        "20 Jan 1995",
                        List.of("Maths", "Physics"),
                        List.of("Sports", "Reading"),
                        "123, MG Road, Chennai",
                        "NCR", "Delhi"
                }
        };
    }

    @DataProvider(name = "genderHobbyMatrix")
    public Object[][] genderHobbyMatrix() {
        return new Object[][]{
                {"Male",   List.of("Sports")},
                {"Female", List.of("Music")},
                {"Other",  List.of("Reading")}
        };
    }
}
