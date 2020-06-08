package ir.aminer.potadoshack.core.network.packets;

import ir.aminer.potadoshack.core.error.Error;

public class ErrorPacket extends ResponsePacket{
    private final Error error;

    public ErrorPacket(Error error) {
        super(null, Status.ERROR);
        this.error = error;
    }

    public Error getError() {
        return error;
    }

    @Override
    public int getId() {
        return -1;
    }
}
