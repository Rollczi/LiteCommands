package dev.rollczi.litecommands.modern.command.contextual.warpped;

import dev.rollczi.litecommands.modern.command.contextual.ExpectedContextual;
import dev.rollczi.litecommands.modern.command.contextual.ExpectedContextualProvider;
import panda.std.Option;

public interface WrappedExpectedContextualFactory {

    <EXPECTED> WrappedExpectedContextual<EXPECTED> wrap(
        ExpectedContextualProvider<EXPECTED> expectedContextualProvider,
        ExpectedContextual<EXPECTED> context
    );

    <EXPECTED> Option<WrappedExpectedContextual<EXPECTED>> empty(ExpectedContextual<EXPECTED> context);

    Class<?> getWrapperType();

}
