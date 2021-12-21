package dev.rollczi.litecommands.inject;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.valid.ValidationCommandException;

import java.util.List;

public interface SingleArgumentHandler<T> extends ArgumentHandler<T> {

    @Override
    default T parse(InjectContext context, int rawIndex) throws ValidationCommandException {
        LiteInvocation invocation = context.getInvocation();
        String argument = invocation.arguments()[context.getArgsMargin() + rawIndex];

        return parse(invocation, argument);
    }

    T parse(LiteInvocation invocation, String argument) throws ValidationCommandException;

    @Override
    List<String> tabulation(String command, String[] args);

}
