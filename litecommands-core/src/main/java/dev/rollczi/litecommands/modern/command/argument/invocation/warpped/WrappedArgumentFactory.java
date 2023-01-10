package dev.rollczi.litecommands.modern.command.argument.invocation.warpped;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResult;

public interface WrappedArgumentFactory {

    <DETERMINANT, EXPECTED> WrappedArgumentWrapper<EXPECTED> wrap(
        ArgumentResult<EXPECTED> result,
        ArgumentContext<DETERMINANT, EXPECTED> context
    );

    Class<?> getWrapperType();

}
