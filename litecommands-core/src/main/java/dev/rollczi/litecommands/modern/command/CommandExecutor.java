package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.modern.argument.PreparedArgumentImpl;
import dev.rollczi.litecommands.modern.argument.FailedReason;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.wrapper.WrapperFormat;
import panda.std.Result;

import java.util.List;

public interface CommandExecutor<SENDER> {

    List<PreparedArgumentImpl<SENDER, ?>> arguments();

    List<WrapperFormat<?>> contextuals();

    Result<CommandExecuteResult, FailedReason> execute(Invocation<SENDER> invocation, InvokedWrapperInfoResolver<SENDER> invokedWrapperInfoResolver, PreparedArgumentIterator<SENDER> cachedArgumentResolver);

}
