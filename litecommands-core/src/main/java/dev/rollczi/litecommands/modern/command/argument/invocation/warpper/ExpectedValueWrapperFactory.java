package dev.rollczi.litecommands.modern.command.argument.invocation.warpper;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResult;

public interface ExpectedValueWrapperFactory {

    <DETERMINANT, EXPECTED> ExpectedValueWrapper<EXPECTED> wrap(
        ArgumentResult<EXPECTED> result,
        ArgumentContext<DETERMINANT, EXPECTED> context
    );

    Class<?> getWrapperType();

}
