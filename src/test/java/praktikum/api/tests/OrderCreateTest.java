package praktikum.api.tests;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.api.client.OrderClient;
import praktikum.api.client.UserClient;
import praktikum.api.model.CreateOrderRequest;
import praktikum.api.model.CreateUserRequest;
import praktikum.api.model.CreateOrderResponse;
import praktikum.api.model.IngredientsResponse;
import praktikum.api.model.SuccessResponse;
import praktikum.api.utils.UserGenerator;

import java.util.Arrays;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class OrderCreateTest {

    private UserClient userClient;
    private OrderClient orderClient;
    private String accessToken;
    private String ingredientHash;

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        CreateUserRequest userRequest = UserGenerator.getRandomUser();
        accessToken = userClient.create(userRequest)
                .extract()
                .as(SuccessResponse.class).accessToken;

        IngredientsResponse ingredientsResponse = orderClient.getIngredients()
                .statusCode(SC_OK)
                .extract()
                .as(IngredientsResponse.class);

        if (ingredientsResponse.data != null && !ingredientsResponse.data.isEmpty()) {
            ingredientHash = ingredientsResponse.data.get(0)._id;
        }
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken).statusCode(SC_ACCEPTED);
        }
    }

    @Test
    @DisplayName("Create order with auth and ingredients returns 200")
    public void createOrderWithAuthAndIngredientsReturns200() {
        CreateOrderRequest orderRequest = new CreateOrderRequest(Arrays.asList(ingredientHash));
        var response = orderClient.create(orderRequest, accessToken)
                .statusCode(SC_OK)
                .extract()
                .as(CreateOrderResponse.class);

        assertTrue("Order should be created successfully", response.success);
        assertNotNull("Order number should not be null", response.order.number);
    }

    @Test
    @DisplayName("Create order without auth returns 200")
    public void createOrderWithoutAuthReturns200() {
        CreateOrderRequest orderRequest = new CreateOrderRequest(Arrays.asList(ingredientHash));
        var response = orderClient.create(orderRequest, null)
                .statusCode(SC_OK)
                .extract()
                .as(CreateOrderResponse.class);

        assertTrue("Order should be created successfully even without auth", response.success);
        assertNotNull("Order number should not be null", response.order.number);
    }

    @Test
    @DisplayName("Create order without ingredients returns 400")
    public void createOrderWithoutIngredientsReturns400() {
        CreateOrderRequest orderRequest = new CreateOrderRequest(Arrays.asList());
        var response = orderClient.create(orderRequest, accessToken)
                .statusCode(SC_BAD_REQUEST)
                .extract()
                .as(praktikum.api.model.ErrorResponse.class);

        assertFalse("Order creation without ingredients should fail", response.success);
        assertEquals("Error message should be correct", "Ingredient ids must be provided", response.message);
    }

    @Test
    @DisplayName("Create order with invalid ingredient hash returns 400")
    public void createOrderWithInvalidHashReturns400() {
        CreateOrderRequest orderRequest = new CreateOrderRequest(Arrays.asList("invalid_hash"));
        var response = orderClient.create(orderRequest, accessToken)
                .statusCode(SC_BAD_REQUEST)
                .extract()
                .as(praktikum.api.model.ErrorResponse.class);

        assertFalse("Order creation with invalid hash should fail", response.success);
    }
}