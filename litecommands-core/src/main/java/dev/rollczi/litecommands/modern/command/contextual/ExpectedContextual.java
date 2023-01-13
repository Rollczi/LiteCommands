package dev.rollczi.litecommands.modern.command.contextual;

public interface ExpectedContextual<EXPECTED> {

    Class<EXPECTED> getExpectedType();

    Class<?> getExpectedWrapperType();

}
