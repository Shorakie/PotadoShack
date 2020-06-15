package ir.aminer.potadoshack.core.event.events;

import ir.aminer.potadoshack.core.auth.simplejwt.JWT;
import ir.aminer.potadoshack.core.auth.simplejwt.UserPayload;
import ir.aminer.potadoshack.core.error.Error;
import ir.aminer.potadoshack.core.network.ClientSocket;
import ir.aminer.potadoshack.core.network.packets.AuthenticatedPacket;
import ir.aminer.potadoshack.core.network.packets.Packet;
import ir.aminer.potadoshack.server.User;

import java.io.IOException;

public abstract class AuthenticatedEvent extends Event {
    public AuthenticatedEvent(Packet packet, ClientSocket sender) {
        super(packet, sender);
    }

    public User getUser() throws IOException {
        JWT jwt = ((AuthenticatedPacket) getData()).getJwt();

        User client;
        try {
            client = User.fromJWT(jwt);
        } catch (IllegalStateException exception) {
            getSender().sendError(Error.UNAUTHORIZED_TOKEN);
            getSender().close();
            return null;
        }

        if (!client.getPassword().equals(((UserPayload) jwt.getPayload()).getPassword()))
            getSender().sendError(Error.UNAUTHORIZED_TOKEN);

        return client;
    }
}
