package ir.aminer.potadoshack.server;

import ir.aminer.potadoshack.core.eventsystem.EventHandler;
import ir.aminer.potadoshack.core.eventsystem.ListenerMethod;
import ir.aminer.potadoshack.core.eventsystem.Listener;
import ir.aminer.potadoshack.core.eventsystem.events.Event;
import ir.aminer.potadoshack.core.eventsystem.events.SignInEvent;
import ir.aminer.potadoshack.core.eventsystem.events.SignUpEvent;
import ir.aminer.potadoshack.server.listeneres.AuthenticationListener;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

public class PotadoShackServer {
    public static final String SECRET_KEY = "P0t4doS3rver";
    private ServerSocket serverSocket;
    private Socket client;

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
            client = serverSocket.accept();
            System.out.println(client.getInetAddress().getHostName()+"@"+client.getInetAddress().getHostAddress() + "# Accepted.");
            new ClientThread(client).start();
        }
    }
}
