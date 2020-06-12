package ir.aminer.potadoshack.server.command;

import java.util.Objects;

public abstract class Command {
    public abstract void execute();
    public abstract String help();

    protected abstract String getCode();

    @Override
    public int hashCode() {
        return Objects.hash(getCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return true;
    }
}
