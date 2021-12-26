package dev.rollczi.litecommands.inject;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.component.LiteComponent;
import dev.rollczi.litecommands.valid.ValidationCommandException;

import java.util.Collections;
import java.util.List;

public interface RelatedLastArgumentHandler<T> extends ArgumentHandler<T> {

    @Override
    default T parse(LiteComponent.ContextOfResolving context, int rawIndex) throws ValidationCommandException {
        LiteInvocation invocation = context.getInvocation();
        String[] arguments = invocation.arguments();
        int argumentIndex = context.getArgsMargin() + rawIndex;

        String argument = arguments[argumentIndex];

        if (argumentIndex - (context.getArgsMargin() + 1) < 0) {
            return singleParse(invocation, argument);
        }

        String lastArgument = arguments[argumentIndex - 1];

        return parse(invocation, lastArgument, argument);
    }

    default T singleParse(LiteInvocation invocation, String argument) throws ValidationCommandException {
        return null;
    }

    T parse(LiteInvocation invocation, String lastArgument, String argument) throws ValidationCommandException;

    @Override
    default List<String> tabulation(LiteComponent.ContextOfResolving context) {
        LiteInvocation invocation = context.getInvocation();
        String[] arguments = invocation.arguments();

        if (arguments.length - (context.getArgsMargin() + 1) < 2) {
            return singleTabulation(invocation.alias(), arguments);
        }

        return relatedTabulation(invocation.alias(), arguments, arguments[arguments.length - 2]);
    }

    default List<String> singleTabulation(String command, String[] args) {
        return Collections.emptyList();
    }

    List<String> relatedTabulation(String command, String[] args, String lastArgument);

}
