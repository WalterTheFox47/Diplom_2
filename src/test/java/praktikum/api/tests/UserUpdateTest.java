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

public class UserUpdateTest {

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
    @DisplayName("Update user data with auth returns 403")
    public void updateUserDataWithAuthReturns403() {
        CreateUserRequest updateRequest = new CreateUserRequest("newemail@yandex.ru", "newpassword", "NewName");
        var response = userClient.update(accessToken, updateRequest)
                .statusCode(SC_FORBIDDEN)
                .extract()
                .as(praktikum.api.model.ErrorResponse.class);

        assertFalse("Update should fail with 403", response.success);
    }

    @Test
    @DisplayName("Update user data without auth returns 401")
    public void updateUserDataWithoutAuthReturns401() {
        CreateUserRequest updateRequest = new CreateUserRequest("newemail@yandex.ru", "newpassword", "NewName");
        var response = userClient.update(null, updateRequest)
                .statusCode(SC_UNAUTHORIZED)
                .extract()
                .as(praktikum.api.model.ErrorResponse.class);

        assertFalse("Update without auth should fail", response.success);
        assertEquals("Error message should be correct", "You should be authorised", response.message);
    }
}