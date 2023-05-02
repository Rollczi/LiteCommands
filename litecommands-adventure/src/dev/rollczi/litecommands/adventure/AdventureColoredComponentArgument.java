package dev.rollczi.litecommands.adventure;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.argument.resolver.OneArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;

class AdventureColoredComponentArgument<SENDER> extends OneArgumentResolver<SENDER, Component> {

    private final ComponentSerializer<Component, ?, String> kyoriComponentSerializer;

    public AdventureColoredComponentArgument(ComponentSerializer<Component, ?, String> kyoriComponentSerializer) {
        this.kyoriComponentSerializer = kyoriComponentSerializer;
    }

    @Override
    protected ArgumentResult<Component> parse(Invocation<SENDER> invocation, Argument<Component> context, String argument) {
        return ArgumentResult.success(this.kyoriComponentSerializer.deserialize(argument));
    }

}