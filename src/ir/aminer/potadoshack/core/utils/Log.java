package ir.aminer.potadoshack.core.utils;

import ir.aminer.potadoshack.core.network.ClientSocket;

public class Log {
    public static void info(ClientSocket socket, String message){
        System.out.println("["+socket.getAddress()+"]# "+ message);
    }
}
