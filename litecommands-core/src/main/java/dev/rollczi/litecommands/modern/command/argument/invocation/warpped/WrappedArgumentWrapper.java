package dev.rollczi.litecommands.modern.command.argument.invocation.warpped;

public interface WrappedArgumentWrapper<EXPECTED> {

    Object unwrap();

    Class<EXPECTED> getExpectedType();

}
