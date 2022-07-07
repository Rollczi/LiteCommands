package dev.rollczi.litecommands.velocity;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import panda.std.Result;

@ArgumentName("text")
class KyoriColoredComponentArgument implements OneArgument<Component> {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.builder()
            .postProcessor(new LegacyProcessor())
            .build();

    @Override
    public Result<Component, ?> parse(LiteInvocation invocation, String argument) {
        return Result.ok(MINI_MESSAGE.deserialize(argument));
    }

    @Override
    public boolean validate(LiteInvocation invocation, Suggestion suggestion) {
        return true;
    }

}
