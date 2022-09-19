package dev.rollczi.litecommands.argument.basictype;

import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import panda.std.Result;

import java.util.List;

public class StringArgument implements OneArgument<String> {

    @Override
    public Result<String, ?> parse(LiteInvocation invocation, String argument) {
        return Result.ok(argument);
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return TypeUtils.suggestion(invocation, TypeUtils.STRING_SUGGESTION);
    }

    @Override
    public boolean validate(LiteInvocation invocation, Suggestion suggestion) {
        return true;
    }

}
