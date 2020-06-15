package ir.aminer.potadoshack.core.network;

import java.io.IOException;
import java.net.Socket;

public class ServerSocket {
    private static Socket client = null;

    private static void createConnection() {
        if (client == null)
            throw new IllegalStateException("Client Socket is not initialized.");
        if (!client.isConnected())
            throw new IllegalStateException("Client Socket is not connected.");

        String host = client.getInetAddress().getHostAddress();
        int port = client.getPort();
        System.out.println(host + ":" + port);

        createConnection(host, port);
    }

    private static void createConnection(String host, int port) {
        try {
            client = new Socket(host, port);
            client.setKeepAlive(true);
        } catch (IOException e) {
            System.err.println("Couldn't initialize a client socket.");
        }
    }

    public static void awake(String host, int port) {
        if (client != null)
            throw new IllegalStateException("Client Socket is already initialized.");

        createConnection(host, port);
    }

    public static Socket getClient() {
        if (client == null)
            throw new IllegalStateException("Client Socket is not initialized.");

        if (client.isClosed() || client.isOutputShutdown() || client.isInputShutdown()) {
            System.out.println("Closed");
            createConnection();
        }
        return client;
    }
}
