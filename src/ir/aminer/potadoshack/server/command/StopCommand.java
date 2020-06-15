package ir.aminer.potadoshack.server.command;

import ir.aminer.potadoshack.client.PotadoShack;
import ir.aminer.potadoshack.server.PotadoShackServer;

import java.time.chrono.IsoEra;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StopCommand extends Command {

    private final PotadoShackServer server;

    public StopCommand(PotadoShackServer server) {
        this.server = server;
    }

    @Override
    public void execute() {
        server.stop();
    }

    @Override
    public String getCode() {
        return "stop";
    }

    @Override
    public String help() {
        return "stop\t Stops the server.";
    }
}
