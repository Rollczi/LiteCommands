package dev.rollczi.litecommands.wrapper;

public interface Wrappable<T> {

    WrapperFormat<T, ?> getWrapperFormat();

}
