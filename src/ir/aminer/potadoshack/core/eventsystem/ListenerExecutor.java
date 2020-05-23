package ir.aminer.potadoshack.core.eventsystem;

import ir.aminer.potadoshack.core.eventsystem.events.Event;

public abstract class ListenerExecutor {
    public Listener listener;

    public ListenerExecutor(Listener listener) {
        this.listener = listener;
    }

    public abstract void execute(Event event);
}
