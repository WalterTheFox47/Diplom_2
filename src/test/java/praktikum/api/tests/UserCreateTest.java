package praktikum.api.tests;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.api.client.UserClient;
import praktikum.api.model.CreateUserRequest;
import praktikum.api.model.SuccessResponse;
import praktikum.api.utils.UserGenerator;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class UserCreateTest {

    private UserClient userClient;
    private CreateUserRequest userRequest;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken).statusCode(SC_ACCEPTED);
        }
    }

    @Test
    @DisplayName("Create unique user")
    public void createUniqueUserReturns200() {
        userRequest = UserGenerator.getRandomUser();
        var response = userClient.create(userRequest)
                .statusCode(SC_OK)
                .extract()
                .as(SuccessResponse.class);

        assertTrue("User should be created successfully", response.success);
        assertNotNull("Access token should not be null", response.accessToken);
        accessToken = response.accessToken;
    }

    @Test
    @DisplayName("Create duplicate user returns 403")
    public void createDuplicateUserReturns403() {
        userRequest = UserGenerator.getRandomUser();
        accessToken = userClient.create(userRequest)
                .extract()
                .as(SuccessResponse.class).accessToken;

        var response = userClient.create(userRequest)
                .statusCode(SC_FORBIDDEN)
                .extract()
                .as(praktikum.api.model.ErrorResponse.class);

        assertFalse("Creating duplicate user should fail", response.success);
        assertEquals("Error message should be correct", "User already exists", response.message);
    }

    @Test
    @DisplayName("Create user without email returns 403")
    public void createUserWithoutEmailReturns403() {
        userRequest = new CreateUserRequest(null, "1234", "Name");
        var response = userClient.create(userRequest)
                .statusCode(SC_FORBIDDEN)
                .extract()
                .as(praktikum.api.model.ErrorResponse.class);

        assertFalse("Creating user without email should fail", response.success);
        assertEquals("Error message should be correct", "Email, password and name are required fields", response.message);
    }
}