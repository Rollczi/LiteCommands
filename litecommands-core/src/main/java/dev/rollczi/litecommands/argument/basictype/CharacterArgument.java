package dev.rollczi.litecommands.argument.basictype;

import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import panda.std.Option;
import panda.std.Result;

import java.util.List;

import static panda.std.Blank.BLANK;

public class CharacterArgument implements OneArgument<Character> {

    @Override
    public Result<Character, ?> parse(LiteInvocation invocation, String argument) {
        return Option.of(argument)
                .filter(arg -> arg.length() == 1)
                .map(arg -> arg.charAt(0))
                .toResult(BLANK);
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return TypeUtils.suggestion(invocation, TypeUtils.CHARACTER_SUGGESTION);
    }

    @Override
    public boolean validate(LiteInvocation invocation, Suggestion suggestion) {
        int suggestionLength = suggestion.single().length();

        return suggestionLength == 0 || suggestionLength == 1;
    }

}
