package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.argument.FailedReason;
import dev.rollczi.litecommands.argument.PreparedArgument;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.wrapper.WrappedExpected;
import panda.std.Result;

import java.util.function.Supplier;

public interface PreparedArgumentIterator<SENDER> {

    <EXPECTED> Result<Supplier<WrappedExpected<EXPECTED>>, FailedReason> resolveNext(Invocation<SENDER> invocation, PreparedArgument<SENDER, EXPECTED> preparedArgument);

}
