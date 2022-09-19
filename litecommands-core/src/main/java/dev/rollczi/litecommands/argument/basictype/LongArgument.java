package dev.rollczi.litecommands.argument.basictype;

import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import panda.std.Result;

import java.util.List;

public class LongArgument implements OneArgument<Long> {

    @Override
    public Result<Long, ?> parse(LiteInvocation invocation, String argument) {
        return TypeUtils.parse(() -> Long.parseLong(argument));
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return TypeUtils.suggestion(invocation, TypeUtils.NUMBER_SUGGESTION);
    }

    @Override
    public boolean validate(LiteInvocation invocation, Suggestion suggestion) {
        return TypeUtils.validate(Long::parseLong, suggestion);
    }

}
