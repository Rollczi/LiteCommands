package dev.rollczi.litecommands.modern.argument;

public interface ArgumentContext<DETERMINANT, EXPECTED> {

    Argument getArgument();

    DETERMINANT getContext();

    Class<DETERMINANT> getContextType();

    Class<EXPECTED> getReturnType();

}
