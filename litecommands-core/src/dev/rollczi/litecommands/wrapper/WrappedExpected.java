package dev.rollczi.litecommands.wrapper;

public interface WrappedExpected<EXPECTED> {

    Object unwrap();

    Class<EXPECTED> getExpectedType();

}
