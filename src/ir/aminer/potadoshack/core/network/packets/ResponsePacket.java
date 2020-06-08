package ir.aminer.potadoshack.core.network.packets;

public class ResponsePacket extends Packet{
    public enum Status{
        OK,ERROR;
    }

    private Packet response;
    private Status status;

    public ResponsePacket(Packet response, Status status) {
        this.status = status;
        this.response = response;
    }

    public Status getStatus() {
        return status;
    }

    public Packet getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return getResponse().toString();
    }

    @Override
    public int getId() {
        return 0;
    }
}
