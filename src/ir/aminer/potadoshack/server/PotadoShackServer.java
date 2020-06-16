package ir.aminer.potadoshack.server;

import ir.aminer.potadoshack.Main;
import ir.aminer.potadoshack.core.event.EventHandler;
import ir.aminer.potadoshack.core.network.ClientSocket;
import ir.aminer.potadoshack.core.utils.Log;
import ir.aminer.potadoshack.server.command.Command;
import ir.aminer.potadoshack.server.command.HelpCommand;
import ir.aminer.potadoshack.server.command.StopCommand;
import ir.aminer.potadoshack.server.listeneres.AuthenticationListener;
import ir.aminer.potadoshack.server.listeneres.OrdersListener;
import ir.aminer.potadoshack.server.listeneres.UpdatesListener;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class PotadoShackServer {
    public static final String SECRET_KEY = Main.secretKey;
    public static final int threadPoolSize = Main.threadPoolSize;
    private final int port;
    private final ExecutorService executor;
    private final AtomicBoolean running = new AtomicBoolean();

    private ServerSocket serverSocket;
    private ClientSocket client;

    public final Map<String, Command> commands = new HashMap<>(){{
        put("h", new HelpCommand(this));
        put("help", new HelpCommand(this));
        put("stop", new StopCommand(PotadoShackServer.this));
    }};

    public PotadoShackServer(int port) {
        this.port = port;
        this.running.set(false);

        this.executor = Executors.newFixedThreadPool(threadPoolSize);
    }

    public void start() throws IOException {
        if (running.get())
            return;
        running.set(true);

        System.out.println("Starting Server on " + "localhost" + ":" + port);
        serverSocket = new ServerSocket(port);

        /* Create Clients dir if not exists */
        File clientsDir = new File("./clients/");
        if (!clientsDir.exists())
            clientsDir.mkdir();

        /* Awake event handler to create a new instance of it */
        EventHandler.awake();
        EventHandler.getInstance().register(new AuthenticationListener());
        EventHandler.getInstance().register(new OrdersListener());
        EventHandler.getInstance().register(new UpdatesListener());

        /* Fix prompt */
        Log.setAfter(()->System.out.print("Server#"));

        System.out.println("Started Listening.");
        new Thread(() -> {
            while (running.get()) {
                try {
                    client = new ClientSocket(serverSocket.accept());
                    Log.info(client, "Accepted.");
                    executor.submit(new ClientThread(client));
                } catch (SocketException ignored) {
                    System.out.println("Server closed.");
                } catch (IOException ignored) {
                    System.err.println("Could not accept a client.");
                }
            }
        }).start();


        while (running.get())
            commands.getOrDefault(readNextCmd(), new HelpCommand(commands)).execute();
    }

    private String readNextCmd(){
        System.out.print("Server#");
        return new Scanner(System.in).nextLine();
    }

    public void stop() {
        if (!running.get())
            return;
        this.running.set(false);
        try {
            serverSocket.close();
        } catch (IOException ioException) {
            System.err.println("Could not stop the server");
        }
        executor.shutdown();
    }
}
