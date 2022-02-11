package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.component.LiteComponent;
import dev.rollczi.litecommands.valid.ValidationCommandException;
import panda.std.Option;

public interface OptionArgumentHandler<T> extends SingleArgumentHandler<Option<T>> {

    @Override
    default Option<T> parse(LiteComponent.ContextOfResolving context, int rawIndex) throws ValidationCommandException {
        LiteInvocation invocation = context.getInvocation();
        int index = context.getArgsMargin() + rawIndex;
        String[] arguments = invocation.arguments();

        if (arguments.length <= index) {
            return Option.none();
        }

        return parse(invocation, arguments[index]);
    }

    @Override
    default Option<T> parse(LiteInvocation invocation, String argument) throws ValidationCommandException {
        return Option.of(parseIfPresent(invocation, argument));
    }

    T parseIfPresent(LiteInvocation invocation, String argument) throws ValidationCommandException;

}
