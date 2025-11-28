package praktikum.api.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.api.model.CreateUserRequest;
import praktikum.api.model.LoginRequest;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseClient {
    private static final String USER_PATH = "/api/auth/";

    @Step("Create user {userRequest.email}")
    public ValidatableResponse create(CreateUserRequest userRequest) {
        return given()
                .spec(getBaseSpec())
                .body(userRequest)
                .when()
                .post(USER_PATH + "register")
                .then();
    }

    @Step("Login user {loginRequest.email}")
    public ValidatableResponse login(LoginRequest loginRequest) {
        return given()
                .spec(getBaseSpec())
                .body(loginRequest)
                .when()
                .post(USER_PATH + "login")
                .then();
    }

    @Step("Delete user")
    public ValidatableResponse delete(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .delete(USER_PATH + "user")
                .then();
    }

    @Step("Update user data")
    public ValidatableResponse update(String accessToken, CreateUserRequest userRequest) {
        var requestSpec = given().spec(getBaseSpec()).body(userRequest);

        if (accessToken != null) {
            requestSpec.header("Authorization", accessToken);
        }

        return requestSpec
                .when()
                .patch(USER_PATH + "user")
                .then();
    }
}