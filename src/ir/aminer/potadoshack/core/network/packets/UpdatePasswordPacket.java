package ir.aminer.potadoshack.core.network.packets;

import ir.aminer.potadoshack.core.user.BaseUser;

public class UpdatePasswordPacket extends AuthenticatedPacket {
    private final String currentPassword;
    private final String password;

    public UpdatePasswordPacket(String jwt, String currentPassword, String password) {
        super(jwt);
        this.password = password;
        this.currentPassword = currentPassword;
    }

    public String getRawPassword() {
        return password;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    @Override
    public int getId() {
        return 7;
    }
}
