package ir.aminer.potadoshack.core.network.packets;

public class ResponsePacket extends Packet{
    public enum Status{
        OK,ERROR;
    }

    private String response;
    private Status status;

    public ResponsePacket(String response, Status status) {
        this.status = status;
        this.response = response;
    }

    public Status getStatus() {
        return status;
    }

    public String getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return getResponse();
    }

    @Override
    public int getId() {
        return 0;
    }
}
