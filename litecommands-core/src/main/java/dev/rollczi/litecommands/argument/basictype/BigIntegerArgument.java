package dev.rollczi.litecommands.argument.basictype;

import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import panda.std.Result;

import java.math.BigInteger;
import java.util.List;

public class BigIntegerArgument implements OneArgument<BigInteger> {

    @Override
    public Result<BigInteger, ?> parse(LiteInvocation invocation, String argument) {
        return TypeUtils.parse(() -> new BigInteger(argument));
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return TypeUtils.suggestion(invocation, TypeUtils.NUMBER_SUGGESTION);
    }

    @Override
    public boolean validate(LiteInvocation invocation, Suggestion suggestion) {
        return TypeUtils.validate(BigInteger::new, suggestion);
    }

}
