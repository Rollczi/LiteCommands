package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.argument.PreparedArgument;
import dev.rollczi.litecommands.command.CommandExecuteResult;
import dev.rollczi.litecommands.command.CommandExecutor;
import dev.rollczi.litecommands.command.CommandExecutorMatchResult;
import dev.rollczi.litecommands.command.PreparedArgumentIterator;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.CommandMeta;

import java.util.Collections;
import java.util.List;

public class TestExecutor<SENDER> implements CommandExecutor<SENDER> {

    private final CommandMeta meta = CommandMeta.create();

    @Override
    public List<PreparedArgument<SENDER, ?>> getArguments() {
        return Collections.emptyList();
    }

    @Override
    public CommandMeta getMeta() {
        return meta;
    }

    @Override
    public CommandExecutorMatchResult match(Invocation<SENDER> invocation, PreparedArgumentIterator<SENDER> cachedArgumentResolver) {
        return CommandExecutorMatchResult.success(() -> CommandExecuteResult.success(null, Void.class));
    }

}
