package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.modern.argument.FailedReason;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.wrapper.WrappedExpected;
import dev.rollczi.litecommands.modern.wrapper.WrapperFormat;
import panda.std.Result;

import java.util.function.Supplier;

public interface InvokedWrapperInfoResolver<SENDER> {

    <EXPECTED> Result<Supplier<WrappedExpected<EXPECTED>>, FailedReason> resolve(Invocation<SENDER> invocation, WrapperFormat<EXPECTED> format);

}
