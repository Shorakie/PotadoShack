package ir.aminer.potadoshack.server.command;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HelpCommand extends Command {

    private final Map<String, Command> commandsMap;

    public HelpCommand(Map<String, Command> commands) {
        this.commandsMap = commands;
    }

    @Override
    public void execute() {
        Set<Command> commands = new HashSet<Command>(commandsMap.values());
        for( Command command : commands)
            System.out.println(command.help());
    }

    @Override
    public String getCode() {
        return "help";
    }

    @Override
    public String help() {
        return "help, h:\t Shows this page.";
    }
}
