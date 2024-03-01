package dev.rollczi.litecommands.event;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class SimpleEventPublisher implements EventPublisher {

    private final Map<Class<? extends Event>, Set<EventListener<?>>> listeners = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <E extends Event> E publish(E event) {
        Set<EventListener<?>> eventListeners = listeners.get(event.getClass());

        if (eventListeners == null) {
            return event;
        }

        for (EventListener<?> eventListener : eventListeners) {
            ((EventListener<E>) eventListener).onEvent(event);
        }

        return event;
    }

    @Override
    public <E extends Event> void subscribe(Class<E> eventClass, EventListener<E> listener) {
        listeners.computeIfAbsent(eventClass, k -> new HashSet<>()).add(listener);
    }

}
