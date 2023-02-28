package dev.rollczi.litecommands.modern.wrapper;

public interface WrappedExpected<EXPECTED> {

    Object unwrap();

    Class<EXPECTED> getExpectedType();

}
