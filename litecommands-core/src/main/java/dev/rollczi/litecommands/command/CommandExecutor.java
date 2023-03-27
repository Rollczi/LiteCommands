package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.argument.ArgumentResolverContext;
import dev.rollczi.litecommands.argument.PreparedArgument;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.CommandMeta;

import java.util.List;

public interface CommandExecutor<SENDER> {

    List<PreparedArgument<SENDER, ?>> getArguments();

    CommandMeta getMeta();

    CommandExecutorMatchResult match(Invocation<SENDER> invocation, ArgumentResolverContext<?> resolverContext);

}
