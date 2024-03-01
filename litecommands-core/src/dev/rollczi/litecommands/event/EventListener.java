package dev.rollczi.litecommands.event;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface EventListener<E extends Event> {

    void onEvent(E event);

}
