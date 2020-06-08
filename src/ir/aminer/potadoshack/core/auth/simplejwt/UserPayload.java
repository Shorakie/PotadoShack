package ir.aminer.potadoshack.core.auth.simplejwt;

public class UserPayload extends Payload {
    private String username;
    private String password;
    private String first_name;
    private String last_name;

    public UserPayload(String username, String password, String firstName, String lastName) {
        super();
        this.username = username;
        this.password = password;
        this.first_name = firstName;
        this.last_name = lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }
}
