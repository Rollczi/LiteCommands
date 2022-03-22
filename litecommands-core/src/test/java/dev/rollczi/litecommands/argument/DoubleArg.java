package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.valid.ValidationCommandException;
import panda.std.Option;

import java.util.Collections;
import java.util.List;

@ArgumentName("amount")
public class DoubleArg implements SingleArgumentHandler<Double> {

    @Override
    public Double parse(LiteInvocation invocation, String argument) throws ValidationCommandException {
        return Option.attempt(NumberFormatException.class, () -> Double.parseDouble(argument))
                .orThrow(() -> new ValidationCommandException(argument + " is not double!"));
    }

    @Override
    public List<String> tabulation(LiteInvocation invocation, String command, String[] args) {
        return Collections.emptyList();
    }

}
