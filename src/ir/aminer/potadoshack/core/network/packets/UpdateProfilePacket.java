package ir.aminer.potadoshack.core.network.packets;

import ir.aminer.potadoshack.core.user.BaseUser;

public class UpdateProfilePacket extends AuthenticatedPacket {
    private final BaseUser user;

    public UpdateProfilePacket(String jwt, BaseUser user) {
        super(jwt);
        this.user = user;
    }

    public BaseUser getUser() {
        return user;
    }

    @Override
    public int getId() {
        return 7;
    }
}
