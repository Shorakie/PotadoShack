package ir.aminer.potadoshack.client.page.callbacks;

import ir.aminer.potadoshack.core.network.packets.ResponsePacket;

import java.io.IOException;

public interface IPageResponse {
    void onResponse(ResponsePacket response) throws IOException;
}
