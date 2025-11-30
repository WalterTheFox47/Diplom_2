package praktikum.api.tests;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.api.client.OrderClient;
import praktikum.api.client.UserClient;
import praktikum.api.model.CreateOrderRequest;
import praktikum.api.model.CreateUserRequest;
import praktikum.api.model.IngredientsResponse;
import praktikum.api.model.SuccessResponse;
import praktikum.api.utils.UserGenerator;

import java.util.Arrays;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class OrderGetTest {

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
        orderClient.create(new CreateOrderRequest(Arrays.asList(ingredientHash)), accessToken);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken).statusCode(SC_ACCEPTED);
        }
    }

    @Test
    @DisplayName("Get orders for authorized user returns 200")
    public void getOrdersForAuthorizedUserReturns200() {
        var response = orderClient.getUserOrders(accessToken)
                .statusCode(SC_OK)
                .extract()
                .as(praktikum.api.model.OrderResponse.class);

        assertTrue("Getting orders should be successful", response.success);
        assertNotNull("Orders list should not be null", response.orders);
        assertFalse("Orders list should not be empty", response.orders.isEmpty());
    }

    @Test
    @DisplayName("Get orders for unauthorized user returns 401")
    public void getOrdersForUnauthorizedUserReturns401() {
        var response = orderClient.getUserOrders(null)
                .statusCode(SC_UNAUTHORIZED)
                .extract()
                .as(praktikum.api.model.ErrorResponse.class);

        assertFalse("Getting orders without auth should fail", response.success);
        assertEquals("Error message should be correct", "You should be authorised", response.message);
    }
}