package dev.rollczi.litecommands.argument.basictype;

import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import panda.std.Option;
import panda.std.Result;

import java.util.List;

import static panda.std.Blank.BLANK;

public class BooleanArgument implements OneArgument<Boolean> {

    @Override
    public Result<Boolean, ?> parse(LiteInvocation invocation, String argument) {
        return Option.of(argument)
                .filter(arg -> arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("false"))
                .map(Boolean::parseBoolean)
                .toResult(BLANK);
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return TypeUtils.suggestion(invocation, TypeUtils.BOOLEAN_SUGGESTION);
    }

    @Override
    public boolean validate(LiteInvocation invocation, Suggestion suggestion) {
        String argument = suggestion.single();

        return argument.equalsIgnoreCase("true") || argument.equalsIgnoreCase("false");
    }

}
