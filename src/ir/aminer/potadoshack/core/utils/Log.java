package ir.aminer.potadoshack.core.utils;

import ir.aminer.potadoshack.core.network.ClientSocket;

import java.util.function.Consumer;

public class Log {
    private static Runnable after = () -> {};

    public static void setAfter(Runnable after) {
        Log.after = after;
    }

    public static void info(ClientSocket socket, String message) {
        System.out.println("\r[" + socket.getAddress() + "]# " + message);
        after.run();
    }
}
