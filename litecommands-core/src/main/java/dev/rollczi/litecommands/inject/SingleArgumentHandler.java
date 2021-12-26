package dev.rollczi.litecommands.inject;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.component.LiteComponent;
import dev.rollczi.litecommands.valid.ValidationCommandException;

import java.util.List;

public interface SingleArgumentHandler<T> extends ArgumentHandler<T> {

    @Override
    default T parse(LiteComponent.ContextOfResolving context, int rawIndex) throws ValidationCommandException {
        LiteInvocation invocation = context.getInvocation();
        String argument = invocation.arguments()[context.getArgsMargin() + rawIndex];

        return parse(invocation, argument);
    }

    T parse(LiteInvocation invocation, String argument) throws ValidationCommandException;

    @Override
    default List<String> tabulation(LiteComponent.ContextOfResolving context) {
        LiteInvocation invocation = context.getInvocation();

        return tabulation(invocation.alias(), invocation.arguments());
    }

    List<String> tabulation(String command, String[] args);

}
