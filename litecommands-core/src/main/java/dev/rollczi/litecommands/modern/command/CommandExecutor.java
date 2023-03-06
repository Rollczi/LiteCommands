package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.modern.argument.PreparedArgument;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.meta.CommandMeta;
import dev.rollczi.litecommands.modern.wrapper.WrapperFormat;

import java.util.List;

public interface CommandExecutor<SENDER> {

    List<PreparedArgument<SENDER, ?>> getArguments();

    List<WrapperFormat<?>> getContextuals();

    CommandMeta getMeta();

    CommandExecutorMatchResult match(Invocation<SENDER> invocation, InvokedWrapperInfoResolver<SENDER> invokedWrapperInfoResolver, PreparedArgumentIterator<SENDER> cachedArgumentResolver);

}
