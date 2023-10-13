package dev.rollczi.litecommands.argument.resolver.standard;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("rawtypes")
public class EnumArgumentResolver<SENDER> extends ArgumentResolver<SENDER, Enum> {

    private final Map<Class<?>, SuggestionResult> cachedEnumSuggestions = new ConcurrentHashMap<>();

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
        return cachedEnumSuggestions.computeIfAbsent(argument.getWrapperFormat().getParsedType(), clazz -> {
            final Object[] enumConstants = clazz.getEnumConstants();
            if (enumConstants == null || enumConstants.length == 0) {
                return SuggestionResult.empty();
            }


            return Arrays.stream(enumConstants).map(Object::toString).collect(SuggestionResult.collector());
        });
    }

    @SuppressWarnings("unchecked")
    private Enum getEnum(Class<? extends Enum> clazz, String name) throws IllegalArgumentException {
        return Enum.valueOf(clazz, name);
    }
}
