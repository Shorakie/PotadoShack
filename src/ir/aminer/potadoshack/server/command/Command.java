package ir.aminer.potadoshack.server.command;

import ir.aminer.potadoshack.server.command.Command.Argument;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Command {

    public static class Argument {
        private final String name;
        private final String value;

        public Argument(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public boolean isFlag() {
            return name != null && value == null;
        }

        public boolean isCommand() {
            return name == null && value != null;
        }

        public boolean isSwitch() {
            return name != null && value != null;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "{"+name+":"+value+"}";
        }

        public static List<Argument> parse(List<String> arguments){
            List<Argument> result = new ArrayList<>();
            for (int i =0; i<arguments.size(); i++){
                String arg = arguments.get(i);
                String name;

                if (arg.startsWith("--"))
                    name = arg.substring(2);
                else if (arg.startsWith("-"))
                    name = arg.substring(1);
                else {
                    result.add(new Argument(null, arg));
                    continue;
                }

                if (arguments.get(i + 1).startsWith("-")) {
                    result.add(new Argument(name, null));
                } else {
                    result.add(new Argument(name, arguments.get(i + 1)));
                    i++;
                }
            }
            return result;
        }
    }

    public abstract void execute(List<Argument> arguments);

    public abstract String help();

    protected abstract String getCode();

    @Override
    public int hashCode() {
        return Objects.hash(getCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o != null && getClass() == o.getClass();
    }
}
