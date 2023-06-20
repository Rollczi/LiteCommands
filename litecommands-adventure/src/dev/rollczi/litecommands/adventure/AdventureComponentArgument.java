package dev.rollczi.litecommands.adventure;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import net.kyori.adventure.text.Component;

class AdventureComponentArgument<SENDER> extends ArgumentResolver<SENDER, Component> {

    @Override
    protected ParseResult<Component> parse(Invocation<SENDER> invocation, Argument<Component> context, String argument) {
        return ParseResult.success(Component.text(argument));
    }

}
