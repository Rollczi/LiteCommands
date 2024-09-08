package dev.rollczi.litecommands.event;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface EventPublisher {

    boolean hasSubscribers(Class<? extends Event> eventClass);

    <E extends Event> E publish(E event);

    <E extends Event> void subscribe(Class<E> eventClass, EventListener<E> listener);

}
