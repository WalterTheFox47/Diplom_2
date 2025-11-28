package praktikum.api.model;

import java.util.List;

public class OrderResponse {
    public boolean success;
    public List<Order> orders;
    public int total;
    public int totalToday;

    public static class Order {
        public String _id;
        public Owner owner;
        public String status;
        public String name;
        public String createdAt;
        public String updatedAt;
        public int number;
        public List<String> ingredients;
    }

    public static class Owner {
        public String name;
        public String email;
        public String createdAt;
    }
}