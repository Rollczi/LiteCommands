package dev.rollczi.litecommands.modern.contextual.warpped;

public interface WrappedExpectedContextual<EXPECTED> {

    Object unwrap();

    Class<EXPECTED> getExpectedType();

}
