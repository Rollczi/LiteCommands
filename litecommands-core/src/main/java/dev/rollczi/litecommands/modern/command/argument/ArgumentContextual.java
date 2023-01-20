package dev.rollczi.litecommands.modern.command.argument;

import dev.rollczi.litecommands.modern.command.contextual.ExpectedContextual;

public interface ArgumentContextual<DETERMINANT, EXPECTED> extends ExpectedContextual<EXPECTED> {

    DETERMINANT getDeterminant();

    Class<DETERMINANT> getDeterminantType();

}
