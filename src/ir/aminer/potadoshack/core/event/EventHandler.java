package ir.aminer.potadoshack.core.event;

import ir.aminer.potadoshack.core.event.events.Event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class EventHandler {
    private static EventHandler instance = null;

    private final HashMap<Class<? extends Event>, ArrayList<ListenerExecutor>> callbacks = new HashMap<>();
    private final Set<Listener> listeners = new HashSet<>();

    private Map<Class<? extends Event>, Set<ListenerExecutor>> getListenerExecutors(Listener listener) {
        Map<Class<? extends Event>, Set<ListenerExecutor>> ret = new HashMap<>();
        Set<Method> methods;
        try {
            Method[] publicMethods = listener.getClass().getMethods();
            Method[] privateMethods = listener.getClass().getDeclaredMethods();
            methods = new HashSet<>(publicMethods.length + privateMethods.length, 1.0f);
            for (Method method : publicMethods)
                methods.add(method);

            for (Method method : privateMethods)
                methods.add(method);

        } catch (NoClassDefFoundError e) {
            System.err.println("Has failed to register events for " + listener.getClass() + " because " + e.getMessage() + " does not exist.");
            return ret;
        }

        for (final Method method : methods) {
            final ListenerMethod lm = method.getAnnotation(ListenerMethod.class);
            if (lm == null) continue;

            final Class<?> checkClass;
            if (method.getParameterTypes().length != 1 || !Event.class.isAssignableFrom(checkClass = method.getParameterTypes()[0])) {
                System.err.println(" attempted to register an invalid EventHandler method signature \"" + method.toGenericString() + "\" in " + listener.getClass());
                continue;
            }

            final Class<? extends Event> eventClass = checkClass.asSubclass(Event.class);
            method.setAccessible(true);

            Set<ListenerExecutor> eventSet = ret.computeIfAbsent(eventClass, k -> new HashSet<ListenerExecutor>());

            eventSet.add(new ListenerExecutor(listener) {
                @Override
                public void execute(Event event) {
                    try {
                        method.invoke(listener, event);
                    } catch (InvocationTargetException e) {
                        System.err.println(e.getCause().toString());
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        System.err.println("Could not access method.");
                    }
                }
            });
        }

        return ret;
    }

    public void register(Listener listener) {
        if (listeners.contains(listener))
            throw new IllegalArgumentException("Listener is already been registered.");

        for (Map.Entry<Class<? extends Event>, Set<ListenerExecutor>> executors : getListenerExecutors(listener).entrySet()) {
            callbacks.computeIfAbsent(executors.getKey(), k -> new ArrayList<>());
            callbacks.get(executors.getKey()).addAll(executors.getValue());
        }

        listeners.add(listener);
    }

    public void trigger(Event event) {
        if (callbacks.get(event.getClass()) == null) {
            System.err.println("Event " + event.getClass().getName() + " has no callbacks.");
            return;
        }

        for (ListenerExecutor executor : callbacks.get(event.getClass()))
            executor.execute(event);
    }

    public static void awake() {
        if (instance != null)
            throw new IllegalStateException("EventHandler is already initialized.");

        instance = new EventHandler();
    }

    public static EventHandler getInstance() {
        if (instance == null)
            throw new IllegalStateException("Instance is not initialized.");

        return instance;
    }
}
