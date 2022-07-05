package dev.rollczi.litecommands.bukkit.adventure;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import net.kyori.adventure.text.Component;
import panda.std.Result;

@ArgumentName("text")
class KyoriComponentArgument implements OneArgument<Component> {

    @Override
    public Result<Component, ?> parse(LiteInvocation invocation, String argument) {
        return Result.ok(Component.text(argument));
    }

    @Override
    public boolean validate(LiteInvocation invocation, Suggestion suggestion) {
        return true;
    }

}
