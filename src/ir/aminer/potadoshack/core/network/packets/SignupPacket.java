package ir.aminer.potadoshack.core.network.packets;

public class SignupPacket extends Packet{
    private String username;
    private String password;
    private String first_name;
    private String last_name;

    public SignupPacket(String username, String password, String first_name, String last_name) {
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
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

    @Override
    public int getId() {
        return 2;
    }
}
