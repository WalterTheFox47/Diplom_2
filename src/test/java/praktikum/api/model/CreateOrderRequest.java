package praktikum.api.model;

import java.util.List;

public class CreateOrderRequest {
    public List<String> ingredients;

    public CreateOrderRequest(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}