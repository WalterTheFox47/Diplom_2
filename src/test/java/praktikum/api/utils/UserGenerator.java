package praktikum.api.utils;

import praktikum.api.model.CreateUserRequest;

import java.util.Random;

public class UserGenerator {
    private static final Random random = new Random();

    public static CreateUserRequest getRandomUser() {
        String email = "testuser" + random.nextInt(10000) + "@yandex.ru";
        String password = "password" + random.nextInt(10000);
        String name = "TestUser" + random.nextInt(10000);
        return new CreateUserRequest(email, password, name);
    }
}