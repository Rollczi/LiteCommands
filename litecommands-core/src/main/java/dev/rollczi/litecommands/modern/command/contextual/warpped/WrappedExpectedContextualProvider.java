package dev.rollczi.litecommands.modern.command.contextual.warpped;

import dev.rollczi.litecommands.modern.command.Invocation;
import dev.rollczi.litecommands.modern.command.argument.invocation.FailedReason;
import dev.rollczi.litecommands.modern.command.contextual.ExpectedContextual;
import panda.std.Result;

import java.util.function.Supplier;

public interface WrappedExpectedContextualProvider<SENDER> {

    <EXPECTED> Result<Supplier<WrappedExpectedContextual<EXPECTED>>, FailedReason> provide(Invocation<SENDER> invocation, ExpectedContextual<EXPECTED> expectedContextual);

}
