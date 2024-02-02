package dev.rollczi.litecommands.event;

public interface EventHandler {

    <E> E publish(E event);

    <E> void subscribe(Class<E> eventClass, EventListener<E> listener);

}
