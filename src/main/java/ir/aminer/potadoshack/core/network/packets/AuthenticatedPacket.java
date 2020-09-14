package ir.aminer.potadoshack.core.network.packets;

import ir.aminer.potadoshack.core.auth.simplejwt.JWT;

public abstract class AuthenticatedPacket extends Packet {
    private final String jwt;

    public AuthenticatedPacket(String jwt) {
        this.jwt = jwt;
    }

    public JWT getJwt() {
        return JWT.decode(jwt);
    }
}
