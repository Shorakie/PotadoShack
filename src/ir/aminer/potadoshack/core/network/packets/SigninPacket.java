package ir.aminer.potadoshack.core.network.packets;

public class SignInPacket extends Packet {
    private final String username;
    private final String password;

    public SignInPacket(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public int getId() {
        return 1;
    }
}
