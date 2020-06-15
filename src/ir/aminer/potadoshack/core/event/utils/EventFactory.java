package ir.aminer.potadoshack.core.event.utils;

import ir.aminer.potadoshack.core.event.events.*;
import ir.aminer.potadoshack.core.network.ClientSocket;
import ir.aminer.potadoshack.core.network.packets.*;

import java.util.HashMap;
import java.util.Map;

public class EventFactory {
    private static final Map<Class<? extends Packet>, EventSupplier<? extends Event>> eventsMap = new HashMap<>() {{
        put(ErrorPacket.class, ErrorEvent::new);
        put(SignInPacket.class, SignInEvent::new);
        put(SignUpPacket.class, SignUpEvent::new);
        put(PlaceOrderPacket.class, PlaceOrderEvent::new);
        put(CancelOrderPacket.class, CancelOrderEvent::new);
        put(ViewOrdersPacket.class, ViewOrdersEvent::new);
    }};

    public static <E extends Event> E getEvent(Packet packet, ClientSocket sender) {
        return ((E) eventsMap.get(packet.getClass()).get(packet, sender));
    }
}
