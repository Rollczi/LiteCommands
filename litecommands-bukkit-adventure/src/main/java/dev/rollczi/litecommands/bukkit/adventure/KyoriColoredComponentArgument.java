package dev.rollczi.litecommands.bukkit.adventure;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import panda.std.Result;

@ArgumentName("text")
class KyoriColoredComponentArgument implements OneArgument<Component> {

    private final ComponentSerializer<Component, ?, String> kyoriComponentSerializer;

    public KyoriColoredComponentArgument(ComponentSerializer<Component, ?, String> kyoriComponentSerializer) {
        this.kyoriComponentSerializer = kyoriComponentSerializer;
    }

    @Override
    public Result<Component, ?> parse(LiteInvocation invocation, String argument) {
        return Result.ok(this.kyoriComponentSerializer.deserialize(argument));
    }

    @Override
    public boolean validate(LiteInvocation invocation, Suggestion suggestion) {
        return true;
    }

}
