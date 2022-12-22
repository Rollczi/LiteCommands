package dev.rollczi.litecommands.modern.command.argument.invocation.warpper;

public interface ExpectedValueWrapper<EXPECTED> {

    Object getWrappedValue();

    Class<EXPECTED> getExpectedType();

}
