package dev.rollczi.litecommands.modern.contextual;

public interface ExpectedContextual<EXPECTED> {

    Class<EXPECTED> getExpectedType();

    Class<?> getExpectedWrapperType();

}
