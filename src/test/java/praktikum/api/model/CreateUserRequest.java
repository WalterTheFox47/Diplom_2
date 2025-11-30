package praktikum.api.model;

public class CreateUserRequest {
    public String email;
    public String password;
    public String name;

    public CreateUserRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}