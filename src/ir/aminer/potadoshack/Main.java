package ir.aminer.potadoshack;

import ir.aminer.potadoshack.client.PotadoShack;
import ir.aminer.potadoshack.server.PotadoShackServer;

import java.io.IOException;

public class Main {
    public enum Mode{
        SERVER, CLIENT
    }
    public static Mode mode = Mode.CLIENT;
    public static int port = 25552;
    public static String host = "localhost";

    public static void main(String[] args) throws IOException {

        for (int i=0; i <args.length; i++) {
            String cmd = null;
            if (args[i].startsWith("--"))
                cmd = args[i].substring(2);
            else if (args[i].startsWith("-"))
                cmd = args[i].substring(1);

            if(cmd==null)
                continue;

            switch (cmd) {
                /* if argument was s or server start the server instead */
                case "s":
                case "server":
                    mode = Mode.SERVER;
                    break;
                /* if argument was p or port set the port */
                case "p":
                case "port":
                    port = Integer.parseInt(args[i+1]);
                    break;
                /* if argument was h or host set the host */
                case "h":
                case "host":
                    host = args[i+1];
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }

        if (mode.equals(Mode.CLIENT))
            javafx.application.Application.launch(PotadoShack.class, args);
        else if (mode.equals(Mode.SERVER))
            new PotadoShackServer().start(port);
    }
}
