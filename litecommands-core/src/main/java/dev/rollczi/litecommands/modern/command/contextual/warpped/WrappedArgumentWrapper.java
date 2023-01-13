package dev.rollczi.litecommands.modern.command.contextual.warpped;

public interface WrappedArgumentWrapper<EXPECTED> {

    Object unwrap();

    Class<EXPECTED> getExpectedType();

}
