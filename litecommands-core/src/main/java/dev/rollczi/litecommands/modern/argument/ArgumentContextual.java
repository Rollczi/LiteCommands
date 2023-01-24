package dev.rollczi.litecommands.modern.argument;

import dev.rollczi.litecommands.modern.contextual.ExpectedContextual;

public interface ArgumentContextual<DETERMINANT, EXPECTED> extends ExpectedContextual<EXPECTED> {

    DETERMINANT getDeterminant();

    Class<DETERMINANT> getDeterminantType();

}
