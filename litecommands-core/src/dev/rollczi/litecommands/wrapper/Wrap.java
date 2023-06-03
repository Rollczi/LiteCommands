package dev.rollczi.litecommands.wrapper;

public interface Wrap<EXPECTED> {

    Object unwrap();

    Class<EXPECTED> getExpectedType();

}
