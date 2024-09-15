package dev.rollczi.litecommands.event;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface EventPublisher {

    boolean hasSubscribers(Class<? extends Event> eventClass);

    <E extends Event> E publish(E event);

    void subscribe(EventListener listener);

}
