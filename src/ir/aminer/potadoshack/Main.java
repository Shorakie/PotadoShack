package ir.aminer.potadoshack;

import ir.aminer.potadoshack.client.PotadoShack;
import ir.aminer.potadoshack.server.PotatoShackServer;

public class Main {
    public static void main(String[] args) {
        for (String arg : args) {
            String cmd;
            if (arg.startsWith("--"))
                cmd = arg.substring(2);
            else if (arg.startsWith("-"))
                cmd = arg.substring(1);
            else
                throw new IllegalArgumentException();
            
            switch (cmd) {
                /* if argument was s or server start the server instead */
                case "s":
                case "server":
                    new PotatoShackServer().start();
                    return;
                default:
                    throw new IllegalArgumentException();
            }
        }

        javafx.application.Application.launch(PotadoShack.class, args);
    }
}
