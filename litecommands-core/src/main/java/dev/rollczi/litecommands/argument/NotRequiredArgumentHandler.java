package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.component.LiteComponent;
import dev.rollczi.litecommands.valid.ValidationCommandException;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface NotRequiredArgumentHandler<T> extends SingleArgumentHandler<T> {

    @Override
    default T parse(LiteComponent.ContextOfResolving context, int rawIndex) throws ValidationCommandException {
        LiteInvocation invocation = context.getInvocation();
        int index = context.getArgsMargin() + rawIndex;
        String[] arguments = invocation.arguments();

        if (arguments.length <= index) {
            return orElse(invocation);
        }

        return parse(invocation, arguments[index]);
    }

    T orElse(LiteInvocation invocation) throws ValidationCommandException;

}
