package ir.aminer.potadoshack.server.command;

import ir.aminer.potadoshack.server.PotadoShackServer;

import java.util.List;

public class StopCommand extends Command {

    private final PotadoShackServer server;

    public StopCommand(PotadoShackServer server) {
        this.server = server;
    }

    @Override
    public void execute(List<Argument> arguments) {
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
