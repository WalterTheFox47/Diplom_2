package praktikum.api.tests;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.api.client.UserClient;
import praktikum.api.model.CreateUserRequest;
import praktikum.api.model.LoginRequest;
import praktikum.api.model.SuccessResponse;
import praktikum.api.utils.UserGenerator;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class UserLoginTest {

    private UserClient userClient;
    private CreateUserRequest userRequest;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        userRequest = UserGenerator.getRandomUser();
        accessToken = userClient.create(userRequest)
                .extract()
                .as(SuccessResponse.class).accessToken;
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken).statusCode(SC_ACCEPTED);
        }
    }

    @Test
    @DisplayName("Login with valid credentials returns 200")
    public void loginWithValidCredentialsReturns200() {
        LoginRequest loginRequest = new LoginRequest(userRequest.email, userRequest.password);
        var response = userClient.login(loginRequest)
                .statusCode(SC_OK)
                .extract()
                .as(SuccessResponse.class);

        assertTrue("Login should be successful", response.success);
        assertNotNull("Access token should not be null", response.accessToken);
    }

    @Test
    @DisplayName("Login with invalid password returns 401")
    public void loginWithInvalidPasswordReturns401() {
        LoginRequest loginRequest = new LoginRequest(userRequest.email, "wrongpassword");
        var response = userClient.login(loginRequest)
                .statusCode(SC_UNAUTHORIZED)
                .extract()
                .as(praktikum.api.model.ErrorResponse.class);

        assertFalse("Login with wrong password should fail", response.success);
        assertEquals("Error message should be correct", "email or password are incorrect", response.message);
    }
}