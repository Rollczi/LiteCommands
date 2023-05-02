package dev.rollczi.litecommands.adventure;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.argument.resolver.OneArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import net.kyori.adventure.text.Component;

class AdventureComponentArgument<SENDER> extends OneArgumentResolver<SENDER, Component> {

    @Override
    protected ArgumentResult<Component> parse(Invocation<SENDER> invocation, Argument<Component> context, String argument) {
        return ArgumentResult.success(Component.text(argument));
    }

}
