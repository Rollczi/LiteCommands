package dev.rollczi.litecommands.adventure;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;

class AdventureColoredComponentArgument<SENDER> extends ArgumentResolver<SENDER, Component> {

    private final ComponentSerializer<Component, ?, String> kyoriComponentSerializer;

    public AdventureColoredComponentArgument(ComponentSerializer<Component, ?, String> kyoriComponentSerializer) {
        this.kyoriComponentSerializer = kyoriComponentSerializer;
    }

    @Override
    protected ParseResult<Component> parse(Invocation<SENDER> invocation, Argument<Component> context, String argument) {
        return ParseResult.success(this.kyoriComponentSerializer.deserialize(argument));
    }

}