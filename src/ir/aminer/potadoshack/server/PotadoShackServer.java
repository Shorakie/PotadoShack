package ir.aminer.potadoshack.server;

import ir.aminer.potadoshack.core.event.EventHandler;
import ir.aminer.potadoshack.core.network.ClientSocket;
import ir.aminer.potadoshack.core.utils.Log;
import ir.aminer.potadoshack.server.listeneres.AuthenticationListener;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

public class PotadoShackServer {
    public static final String SECRET_KEY = "P0t4doS3rver";
    private ServerSocket serverSocket;
    private ClientSocket client;

    public void start(int port) throws IOException {
        System.out.println("Starting Server on " + "localhost" + ":" + port);
        serverSocket = new ServerSocket(port);

        /* Create Clients dir if not exists */
        File clientsDir = new File("./clients/");
        if (!clientsDir.exists())
            clientsDir.mkdir();

        /* Awake event handler to create a new instance of it */
        EventHandler.awake();
        EventHandler.getInstance().register(new AuthenticationListener());

        while (true) {
            client = new ClientSocket(serverSocket.accept());
            Log.info(client, "Accepted");
            // TODO: Make Thread pool
            new ClientThread(client).start();
        }
    }
}
