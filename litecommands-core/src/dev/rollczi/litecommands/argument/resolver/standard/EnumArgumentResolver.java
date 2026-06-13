package dev.rollczi.litecommands.argument.resolver.standard;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

import dev.rollczi.litecommands.meta.MetaKey;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("rawtypes")
public class EnumArgumentResolver<SENDER> extends ArgumentResolver<SENDER, Enum> {

    public static final MetaKey<Boolean> CASE_INSENSITIVE = MetaKey.of("enum-case-insensitive", Boolean.class, false);

    private final Map<Class<Enum>, SuggestionResult> cachedEnumSuggestions = new ConcurrentHashMap<>();

    @Override
    protected ParseResult<Enum> parse(Invocation<SENDER> invocation, Argument<Enum> context, String argument) {
        Class<Enum> enumClass = context.getType().getRawType();
        boolean caseInsensitive = Boolean.TRUE.equals(context.metaCollector().findFirst(CASE_INSENSITIVE));

        if (caseInsensitive) {
            return Arrays.stream(enumClass.getEnumConstants())
                .filter(enumConstant -> enumConstant.name().equalsIgnoreCase(argument))
                .findFirst()
                .map(ParseResult::success)
                .orElseGet(() -> ParseResult.failure(InvalidUsage.Cause.INVALID_ARGUMENT));
        }

        try {
            return ParseResult.success(getEnum(enumClass, argument));
        } catch (IllegalArgumentException ignored) {
            return ParseResult.failure(InvalidUsage.Cause.INVALID_ARGUMENT);
        }
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<Enum> argument, SuggestionContext context) {
        Class<Enum> enumClass = argument.getType().getRawType();

        return cachedEnumSuggestions.computeIfAbsent(enumClass, key -> {
            Enum[] enums = enumClass.getEnumConstants();
            if (enums == null || enums.length == 0) {
                return SuggestionResult.empty();
            }

            return Arrays.stream(enums)
                .map(Enum::name)
                .collect(SuggestionResult.collector());
        });
    }

    @SuppressWarnings("unchecked")
    private Enum getEnum(Class<? extends Enum> clazz, String name) throws IllegalArgumentException {
        return Enum.valueOf(clazz, name);
    }

}
