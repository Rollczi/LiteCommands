package dev.rollczi.litecommands.inject;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.valid.ValidationCommandException;

import java.util.Collections;
import java.util.List;

public interface RelatedLastArgumentHandler<T> extends ArgumentHandler<T> {

    @Override
    default T parse(InjectContext context, int rawIndex) throws ValidationCommandException {
        LiteInvocation invocation = context.getInvocation();
        String[] arguments = invocation.arguments();
        int lastArgumentIndex = context.getArgsMargin() + rawIndex - 1;

        if (lastArgumentIndex + 1 >= arguments.length || lastArgumentIndex < 0) {
            return null;
        }

        String lastArgument = arguments[lastArgumentIndex];
        String argument = arguments[lastArgumentIndex + 1];

        return parse(invocation, lastArgument, argument);
    }

    T parse(LiteInvocation invocation, String lastArgument, String argument) throws ValidationCommandException;

    @Override
    default List<String> tabulation(String command, String[] args) {
        if (args.length < 2) {
            return singleTabulation(command, args);
        }

        return relatedTabulation(command, args, args[args.length - 2]);
    }

    default List<String> singleTabulation(String command, String[] args) {
        return Collections.emptyList();
    }

    List<String> relatedTabulation(String command, String[] args, String lastArgument);

}
