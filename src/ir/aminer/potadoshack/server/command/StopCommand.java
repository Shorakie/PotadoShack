package ir.aminer.potadoshack.server.command;

import ir.aminer.potadoshack.server.PotadoShackServer;

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
