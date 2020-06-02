package ir.aminer.potadoshack.core.network;

import ir.aminer.potadoshack.core.network.packets.ErrorPacket;
import ir.aminer.potadoshack.core.error.Error;
import ir.aminer.potadoshack.core.network.packets.Packet;
import ir.aminer.potadoshack.core.network.packets.ResponsePacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocket {

    private final Socket client;

    public ClientSocket(Socket client) {
        this.client = client;
    }

    public ClientSocket(String host, int port) throws IOException {
        this.client = new Socket(host, port);
    }

    public void close() throws IOException {
        client.close();
    }

    public Socket getClient() {
        return client;
    }

    public void sendResponse(String response, ResponsePacket.Status status) throws IOException {
        sendPacket(new ResponsePacket(response, status));
    }

    public void sendError(Error error) throws IOException {
        sendPacket(new ErrorPacket(error));
    }

    public void sendPacket(Packet packet) throws IOException {
        sendObject(packet);
    }

    private void sendObject(Object object) throws IOException {
        if (!client.isConnected())
            throw new IllegalStateException("Socket is closed for: " + client.getInetAddress().getHostAddress() + ":" + client.getPort());

        if (client.isOutputShutdown())
            throw new IllegalStateException("Socket OutStream is shutdown for: " + client.getInetAddress().getHostAddress() + ":" + client.getPort());

        ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
        outputStream.writeObject(object);
        outputStream.flush();
    }

    public ResponsePacket readResponse() throws IOException, ClassNotFoundException {
        return readPacket();
    }

    public <P extends Packet> P readPacket() throws IOException, ClassNotFoundException {
        Object object = readObject();
        if (!(object instanceof Packet))
            return null;

        return (P) object;
    }

    private Object readObject() throws IOException, ClassNotFoundException {
        if (!client.isConnected())
            throw new IllegalStateException("Socket is closed for: " + client.getInetAddress().getHostAddress() + ":" + client.getPort());

        if (client.isInputShutdown())
            throw new IllegalStateException("Socket InStream is shutdown for: " + client.getInetAddress().getHostAddress() + ":" + client.getPort());

        ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());
        return inputStream.readObject();
    }

    public String getAddress(){
        return client.getInetAddress().getHostName()+"@"+client.getInetAddress().getHostAddress();
    }
}
