package ir.aminer.potadoshack.core.auth.simplejwt;

public class UserPayload extends Payload {
    private final String username;
    private final String password;

    public UserPayload(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
