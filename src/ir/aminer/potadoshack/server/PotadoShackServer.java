package ir.aminer.potadoshack.server;

import ir.aminer.potadoshack.client.Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PotadoShackServer {
    private ServerSocket serverSocket;
    private Socket client;

    public void start(int port) throws IOException {
        System.out.println("Starting Server on " + "localhost" + ":" + port);
        serverSocket = new ServerSocket(port);

        while (true) {
            client = serverSocket.accept();
            System.out.println(client.getInetAddress().getHostName()+"@"+client.getInetAddress().getHostAddress() + "# Accepted.");
            new ClientThread(client).start();
        }
    }
}
