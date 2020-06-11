package ir.aminer.potadoshack.server.listeneres;

import ir.aminer.potadoshack.core.auth.simplejwt.UserPayload;
import ir.aminer.potadoshack.core.auth.simplejwt.JWT;
import ir.aminer.potadoshack.core.error.Error;
import ir.aminer.potadoshack.core.event.Listener;
import ir.aminer.potadoshack.core.event.ListenerMethod;
import ir.aminer.potadoshack.core.event.events.SignInEvent;
import ir.aminer.potadoshack.core.event.events.SignUpEvent;
import ir.aminer.potadoshack.core.network.packets.PrimitivePacket;
import ir.aminer.potadoshack.core.network.packets.ResponsePacket;
import ir.aminer.potadoshack.core.utils.Common;
import ir.aminer.potadoshack.core.utils.Log;
import ir.aminer.potadoshack.server.PotadoShackServer;
import ir.aminer.potadoshack.server.User;

import java.io.File;
import java.io.IOException;

public class AuthenticationListener implements Listener {

    @ListenerMethod
    public void onSignup(SignUpEvent event) throws IOException {
        String username = event.getData().getUsername();
        String password = Common.hmacSha256(PotadoShackServer.SECRET_KEY, event.getData().getPassword());
        String first_name = event.getData().getFirstName();
        String last_name = event.getData().getLastName();

        Log.info(event.getSender(), "Checking " + username + ".pref existence");
        File clientFile = new File("./clients/" + username + ".pref");
        /* If a username with the username exists */
        if (clientFile.exists()) {
            event.getSender().sendError(Error.USER_EXISTS);
            return;
        }

        try {
            clientFile.createNewFile();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        User client = new User(
                username,
                password,
                first_name,
                last_name
        );
        client.save(clientFile);

        JWT jwt = JWT.generate(
                new UserPayload(client.getUsername(), client.getPassword(), client.getFirstName(), client.getLastName()),
                PotadoShackServer.SECRET_KEY);
        event.getSender().sendResponse(new PrimitivePacket(jwt.toString()), ResponsePacket.Status.OK);
    }

    @ListenerMethod
    public void onSignin(SignInEvent event) throws IOException {
        String username = event.getData().getUsername();
        String password = Common.hmacSha256(PotadoShackServer.SECRET_KEY, event.getData().getPassword());

        User client = null;
        try {
             client = User.fromUsername(username);
        } catch (IllegalStateException ignored){
            event.getSender().sendError(Error.USERNAME_NOT_FOUND);
            return;
        }

        if (!client.getPassword().equals(password)) {
            event.getSender().sendError(Error.WRONG_PASSWORD);
            return;
        }

        JWT jwt = JWT.generate(
                new UserPayload(client.getUsername(), client.getPassword(), client.getFirstName(), client.getLastName()),
                PotadoShackServer.SECRET_KEY);
        event.getSender().sendResponse(new PrimitivePacket(jwt.toString()), ResponsePacket.Status.OK);
    }
}
