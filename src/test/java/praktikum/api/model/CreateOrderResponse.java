package praktikum.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateOrderResponse {
    public boolean success;
    public String name;
    public Order order;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Order {
        public String _id;
        public Owner owner;
        public String status;
        public String name;
        public String createdAt;
        public String updatedAt;
        public int number;
        public List<IngredientsResponse.Ingredient> ingredients;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Owner {
        public String name;
        public String email;
        public String createdAt;
        public String updatedAt;
    }
}