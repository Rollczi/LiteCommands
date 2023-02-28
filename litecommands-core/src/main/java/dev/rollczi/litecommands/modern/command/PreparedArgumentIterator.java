package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.modern.argument.PreparedArgument;
import dev.rollczi.litecommands.modern.argument.PreparedArgumentImpl;
import dev.rollczi.litecommands.modern.argument.FailedReason;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.wrapper.WrappedExpected;
import panda.std.Result;

import java.util.function.Supplier;

public interface PreparedArgumentIterator<SENDER> {

    <EXPECTED> Result<Supplier<WrappedExpected<EXPECTED>>, FailedReason> resolveNext(Invocation<SENDER> invocation, PreparedArgument<SENDER, EXPECTED> preparedArgument);

}
