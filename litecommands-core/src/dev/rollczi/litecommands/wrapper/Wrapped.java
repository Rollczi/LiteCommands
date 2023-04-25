package dev.rollczi.litecommands.wrapper;

public interface Wrapped<EXPECTED> {

    Object unwrap();

    Class<EXPECTED> getExpectedType();

}
