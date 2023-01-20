package dev.rollczi.litecommands.modern.command.contextual.warpped;

public interface WrappedExpectedContextual<EXPECTED> {

    Object unwrap();

    Class<EXPECTED> getExpectedType();

}
