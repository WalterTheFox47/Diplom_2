package praktikum.api.model;

import java.util.List;

public class IngredientsResponse {
    public boolean success;
    public List<Ingredient> data;

    public static class Ingredient {
        public String _id;
        public String name;
        public String type;
        public int proteins;
        public int fat;
        public int carbohydrates;
        public int calories;
        public int price;
        public String image;
        public String image_mobile;
        public String image_large;
        public int __v;
    }
}