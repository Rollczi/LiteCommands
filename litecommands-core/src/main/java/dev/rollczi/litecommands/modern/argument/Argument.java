package dev.rollczi.litecommands.modern.argument;

public interface Argument<DETERMINANT, EXPECTED> {

    DETERMINANT getDeterminant();

    Class<DETERMINANT> getDeterminantType();

    Class<EXPECTED> getExpectedType();

    Class<?> getExpectedWrapperType();

}
