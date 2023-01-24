package dev.rollczi.litecommands.modern.contextual.warpped;

import dev.rollczi.litecommands.modern.argument.FailedReason;
import dev.rollczi.litecommands.modern.contextual.ExpectedContextual;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import panda.std.Result;

import java.util.function.Supplier;

public interface ExpectedContextualConverter<SENDER> {

    <EXPECTED> Result<Supplier<WrappedExpectedContextual<EXPECTED>>, FailedReason> provide(Invocation<SENDER> invocation, ExpectedContextual<EXPECTED> expectedContextual);

}
