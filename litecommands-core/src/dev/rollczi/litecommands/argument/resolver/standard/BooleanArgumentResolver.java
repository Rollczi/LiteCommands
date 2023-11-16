package dev.rollczi.litecommands.argument.resolver.standard;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

import java.util.Arrays;
import java.util.List;

public class BooleanArgumentResolver<SENDER> extends ArgumentResolver<SENDER, Boolean> {

    private static final List<String> SUGGESTIONS_LIST = Arrays.asList(
        "true", "false"
    );

    @Override
    protected ParseResult<Boolean> parse(Invocation<SENDER> invocation, Argument<Boolean> context, String argument) {
        Boolean parse;
        try {
            if(argument.equalsIgnoreCase("true") || argument.equalsIgnoreCase("1")) {
                parse = Boolean.TRUE;
            }
            else if(argument.equalsIgnoreCase("false") || argument.equalsIgnoreCase("0")) {
                parse = Boolean.FALSE;
            }
            else {
                return ParseResult.failure(InvalidUsage.Cause.INVALID_ARGUMENT);
            }

            return  ParseResult.success(parse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<Boolean> argument, SuggestionContext context) {
        return SuggestionResult.of(SUGGESTIONS_LIST);
    }

}
