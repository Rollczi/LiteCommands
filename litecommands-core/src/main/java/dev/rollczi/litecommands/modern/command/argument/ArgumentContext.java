package dev.rollczi.litecommands.modern.command.argument;

public interface ArgumentContext<DETERMINANT, EXPECTED> {

    Argument getArgument();

    DETERMINANT getDeterminant();

    Class<DETERMINANT> getDeterminantType();

    Class<EXPECTED> getExpectedType();

    Class<?> getExpectedWrapperType();

}
