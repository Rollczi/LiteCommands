package dev.rollczi.litecommands.argument.resolver.standard;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

public class EnumArgumentResolver<SENDER> extends ArgumentResolver<SENDER, Enum> {

    @Override
    protected ParseResult<Enum> parse(Invocation<SENDER> invocation, Argument<Enum> context, String argument) {
        try {
            return ParseResult.success(getEnum(context.getWrapperFormat().getParsedType(), argument));
        } catch (IllegalArgumentException e) {
            return ParseResult.failure(InvalidUsage.Cause.INVALID_ARGUMENT);
        }
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<Enum> argument, SuggestionContext context) {
        return super.suggest(invocation, argument, context);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Enum getEnum(Class<? extends Enum> clazz, String name) throws IllegalArgumentException {
        return Enum.valueOf(clazz, name);
    }
}
