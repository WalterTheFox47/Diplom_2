package praktikum.api.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import praktikum.api.model.CreateOrderRequest;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseClient {
    private static final String ORDER_PATH = "/api/orders";

    @Step("Get ingredients")
    public ValidatableResponse getIngredients() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get("/api/ingredients")
                .then();
    }

    @Step("Create order")
    public ValidatableResponse create(CreateOrderRequest orderRequest, String accessToken) {
        RequestSpecification requestSpec = given()
                .spec(getBaseSpec())
                .body(orderRequest);

        if (accessToken != null) {
            requestSpec.header("Authorization", accessToken);
        }

        return requestSpec
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Get user orders")
    public ValidatableResponse getUserOrders(String accessToken) {
        var requestSpec = given().spec(getBaseSpec());

        if (accessToken != null) {
            requestSpec.header("Authorization", accessToken);
        }

        return requestSpec
                .when()
                .get(ORDER_PATH)
                .then();
    }
}