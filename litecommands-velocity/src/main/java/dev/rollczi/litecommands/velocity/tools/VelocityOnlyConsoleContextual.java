package dev.rollczi.litecommands.velocity.tools;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.contextual.Contextual;
import panda.std.Option;
import panda.std.Result;

public class VelocityOnlyConsoleContextual<MESSAGE> implements Contextual<CommandSource, ConsoleCommandSource> {

    private final MESSAGE onlyConsoleMessage;

    public VelocityOnlyConsoleContextual(MESSAGE onlyConsoleMessage) {
        this.onlyConsoleMessage = onlyConsoleMessage;
    }

    @Override
    public Result<ConsoleCommandSource, Object> extract(CommandSource source, Invocation<CommandSource> invocation) {
        return Option.of(source).is(ConsoleCommandSource.class).toResult(onlyConsoleMessage);
    }

}
