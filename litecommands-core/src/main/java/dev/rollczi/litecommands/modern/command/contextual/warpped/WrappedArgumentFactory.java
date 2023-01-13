package dev.rollczi.litecommands.modern.command.contextual.warpped;

import dev.rollczi.litecommands.modern.command.contextual.ExpectedContextual;
import dev.rollczi.litecommands.modern.command.contextual.ExpectedContextualProvider;
import panda.std.Option;

public interface WrappedArgumentFactory {

    <EXPECTED> WrappedArgumentWrapper<EXPECTED> wrap(
        ExpectedContextualProvider<EXPECTED> expectedContextualProvider,
        ExpectedContextual<EXPECTED> context
    );

    <EXPECTED> Option<WrappedArgumentWrapper<EXPECTED>> empty(ExpectedContextual<EXPECTED> context);

    Class<?> getWrapperType();

}
