package ir.aminer.potadoshack.server;

import ir.aminer.potadoshack.client.Client;
import ir.aminer.potadoshack.core.BaseClient;
import ir.aminer.potadoshack.core.auth.simplejwt.ClientPayload;
import ir.aminer.potadoshack.core.auth.simplejwt.JWT;

import java.io.File;

public class ServerClient extends BaseClient {

    public ServerClient(String username, String password, String first_name, String last_name) {
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public static BaseClient fromClientUsername(String username) {
        return loadClient(new File("./clients/" + username + ".pref"));
    }

    public static BaseClient fromJWT(JWT jwt) {
        return fromClientUsername(((ClientPayload)jwt.getPayload()).getUsername());
    }
}
