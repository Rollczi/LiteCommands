package dev.rollczi.litecommands.modern.command.argument.invocation;

import dev.rollczi.litecommands.modern.command.Invocation;
import dev.rollczi.litecommands.modern.command.contextual.warpped.WrappedArgumentWrapper;
import dev.rollczi.litecommands.modern.command.contextual.ExpectedContextual;
import panda.std.Result;

import java.util.function.Supplier;

public interface WrappedArgumentProvider<SENDER> {

    <EXPECTED> Result<Supplier<WrappedArgumentWrapper<EXPECTED>>, FailedReason> provide(Invocation<SENDER> invocation, ExpectedContextual<EXPECTED> expectedContextual);

}
