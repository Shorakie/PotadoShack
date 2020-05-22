package ir.aminer.potadoshack.server;

import ir.aminer.potadoshack.core.packets.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientThread extends Thread {

    private Socket client;

    public ClientThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            System.err.println("Could not initialize InputStream. closing socket.");
            /* Close the socket */
            try {client.close();} catch (IOException ioException) {System.err.println("Could not close the socket.");}
            /* Finish the Thread */
            return;
        }

        Packet packet = null;
        try {
            packet = (Packet)inputStream.readObject();

        } catch (IOException e) {
            System.err.println("Could not read the packet.");
            return;
        } catch (ClassNotFoundException e) {
            System.err.println("Packet class was not found.");
            return;
        }

        System.out.println(packet.getId());
    }
}
