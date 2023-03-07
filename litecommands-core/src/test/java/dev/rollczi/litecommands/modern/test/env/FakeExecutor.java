package dev.rollczi.litecommands.modern.test.env;

import dev.rollczi.litecommands.modern.argument.PreparedArgument;
import dev.rollczi.litecommands.modern.command.CommandExecuteResult;
import dev.rollczi.litecommands.modern.command.CommandExecutor;
import dev.rollczi.litecommands.modern.command.CommandExecutorMatchResult;
import dev.rollczi.litecommands.modern.command.InvokedWrapperInfoResolver;
import dev.rollczi.litecommands.modern.command.PreparedArgumentIterator;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.meta.CommandMeta;
import dev.rollczi.litecommands.modern.wrapper.WrapperFormat;

import java.util.Collections;
import java.util.List;

public class FakeExecutor<SENDER> implements CommandExecutor<SENDER> {

    private final CommandMeta meta = CommandMeta.create();

    @Override
    public List<PreparedArgument<SENDER, ?>> getArguments() {
        return Collections.emptyList();
    }

    @Override
    public List<WrapperFormat<?>> getContextuals() {
        return Collections.emptyList();
    }

    @Override
    public CommandMeta getMeta() {
        return meta;
    }

    @Override
    public CommandExecutorMatchResult match(Invocation<SENDER> invocation, InvokedWrapperInfoResolver<SENDER> invokedWrapperInfoResolver, PreparedArgumentIterator<SENDER> cachedArgumentResolver) {
        return CommandExecutorMatchResult.success(() -> CommandExecuteResult.success(null, Void.class));
    }

}
