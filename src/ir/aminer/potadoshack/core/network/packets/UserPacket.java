package ir.aminer.potadoshack.core.network.packets;

import ir.aminer.potadoshack.core.user.BaseUser;

public class UserPacket extends Packet {
    private final String jwt;
    private final BaseUser user;

    public UserPacket(String jwt, BaseUser user) {
        this.jwt = jwt;
        this.user = user;
    }

    public String getJwt() {
        return jwt;
    }

    public BaseUser getUser() {
        return user;
    }

    @Override
    public int getId() {
        return 9;
    }
}
