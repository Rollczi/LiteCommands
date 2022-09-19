package dev.rollczi.litecommands.argument.basictype;

import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import panda.std.Result;

import java.util.List;

public class DoubleArgument implements OneArgument<Double> {

    @Override
    public Result<Double, ?> parse(LiteInvocation invocation, String argument) {
        return TypeUtils.parse(() -> Double.parseDouble(argument));
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return TypeUtils.suggestion(invocation, TypeUtils.DECIMAL_SUGGESTION);
    }

    @Override
    public boolean validate(LiteInvocation invocation, Suggestion suggestion) {
        return TypeUtils.validate(Double::parseDouble, suggestion);
    }

}
