package ir.aminer.potadoshack.server;

import ir.aminer.potadoshack.core.error.Error;
import ir.aminer.potadoshack.core.event.EventHandler;
import ir.aminer.potadoshack.core.event.utils.EventFactory;
import ir.aminer.potadoshack.core.network.ClientSocket;
import ir.aminer.potadoshack.core.network.packets.AuthenticatedPacket;
import ir.aminer.potadoshack.core.network.packets.Packet;
import ir.aminer.potadoshack.core.utils.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientThread extends Thread {

    private final ClientSocket client;

    public ClientThread(ClientSocket client) {
        this.client = client;
    }

    @Override
    public void run() {

        Packet packet;
        try {
            packet = client.readPacket();
        } catch (IOException e) {
            System.err.println("Could not read the packet.");
            return;
        } catch (ClassNotFoundException e) {
            System.err.println("Packet class was not found.");
            return;
        }

        Log.info(client, "Received Packet id:"+packet.getId());

        if(checkAuthentications(packet))
            EventHandler.getInstance().trigger(EventFactory.getEvent(packet, client));
        else
            try {
                client.sendError(Error.INVALID_TOKEN);
            } catch (IOException ioException) {
                System.err.println("Could not send invalid_token error packet");
            }

        /* Close the socket after handling */
        try {
            client.close();
        } catch (IOException ioException) {
            System.err.println("Could not close the socket.");
        }
    }

    private boolean checkAuthentications(Packet packet) {
        if (packet instanceof AuthenticatedPacket)
            return ((AuthenticatedPacket) packet).getJwt().validate(PotadoShackServer.SECRET_KEY);
        else
            return true;
    }
}
