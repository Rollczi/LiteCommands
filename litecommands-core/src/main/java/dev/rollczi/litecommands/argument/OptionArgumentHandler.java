package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.component.LiteComponent;
import dev.rollczi.litecommands.valid.ValidationCommandException;
import panda.std.Option;

public interface OptionArgumentHandler<T> extends NotRequiredArgumentHandler<Option<T>> {

    @Override
    default Option<T> parse(LiteComponent.ContextOfResolving context, int rawIndex) throws ValidationCommandException {
        LiteInvocation invocation = context.getInvocation();
        int index = context.getArgsMargin() + rawIndex;
        String[] arguments = invocation.arguments();

        if (arguments.length <= index) {
            return orElse(invocation);
        }

        return parse(invocation, arguments[index]);
    }

    @Override
    default Option<T> parse(LiteInvocation invocation, String argument) throws ValidationCommandException {
        return Option.of(parseIfPresent(invocation, argument));
    }

    @Override
    default Option<T> orElse(LiteInvocation invocation) throws ValidationCommandException {
        return Option.none();
    }

    T parseIfPresent(LiteInvocation invocation, String argument) throws ValidationCommandException;

}
