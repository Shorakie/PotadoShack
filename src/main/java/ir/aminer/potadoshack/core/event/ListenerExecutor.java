package ir.aminer.potadoshack.core.event;

import ir.aminer.potadoshack.core.event.events.Event;

public abstract class ListenerExecutor {
    public Listener listener;

    public ListenerExecutor(Listener listener) {
        this.listener = listener;
    }

    public abstract void execute(Event event);
}
