package ir.aminer.potadoshack.core.network.packets;

import java.io.Serializable;

public abstract class Packet implements Serializable {
    abstract public int getId();
}
