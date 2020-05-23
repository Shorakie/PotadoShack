package ir.aminer.potadoshack.core.auth.simplejwt;

public class ClientPayload extends Payload {
    private String username;
    private String password;

    public ClientPayload(String username, String password) {
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
