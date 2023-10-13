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

    private final Map<Class<Enum>, SuggestionResult> cachedEnumSuggestions = new ConcurrentHashMap<>();

    @Override
    protected ParseResult<Enum> parse(Invocation<SENDER> invocation, Argument<Enum> context, String argument) {
        Class<Enum> enumClass = context.getWrapperFormat().getParsedType();

        try {
            return ParseResult.success(getEnum(enumClass, argument));
        } catch (IllegalArgumentException ignored) {
            return ParseResult.failure(InvalidUsage.Cause.INVALID_ARGUMENT);
        }
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<Enum> argument, SuggestionContext context) {
        Class<Enum> enumClass = argument.getWrapperFormat().getParsedType();

        return cachedEnumSuggestions.computeIfAbsent(enumClass, key -> {
            Enum[] enums = enumClass.getEnumConstants();
            if (enums == null || enums.length == 0) {
                return SuggestionResult.empty();
            }

            return Arrays.stream(enums)
                .map(anEnum -> anEnum.name())
                .collect(SuggestionResult.collector());
        });
    }

    @SuppressWarnings("unchecked")
    private Enum getEnum(Class<? extends Enum> clazz, String name) throws IllegalArgumentException {
        return Enum.valueOf(clazz, name);
    }

}
