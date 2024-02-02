package dev.rollczi.litecommands.event;

public interface EventListener<E> {

    void onEvent(E event);

}
