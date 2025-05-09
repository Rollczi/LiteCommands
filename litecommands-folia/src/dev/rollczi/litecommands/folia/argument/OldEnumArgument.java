package dev.rollczi.litecommands.folia.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class OldEnumArgument extends ArgumentResolver<CommandSender, Object> {

    private final Map<Class<?>, SuggestionResult> cachedOldEnumSuggestions = new HashMap<>();

    @Override
    public boolean canParse(Argument<Object> argument) {
        return OldEnumAccessor.getType().map(type -> type.isAssignableFrom(argument.getType().getRawType()))
            .orElseThrow(() -> new IllegalStateException("OldEnumArgument can't be used without on old bukkit version"));
    }

    @Override
    protected ParseResult<Object> parse(Invocation<CommandSender> invocation, Argument<Object> context, String argument) {
        try {
            return ParseResult.success(OldEnumAccessor.invokeValueOf(context.getType().getRawType(), argument));
        } catch (IllegalArgumentException ignored) {
            return ParseResult.failure(InvalidUsage.Cause.INVALID_ARGUMENT);
        }
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<Object> argument, SuggestionContext context) {
        Class<?> oldEnumClass = argument.getType().getRawType();

        return cachedOldEnumSuggestions.computeIfAbsent(oldEnumClass, key -> {
            Object[] oldEnums = OldEnumAccessor.invokeValues(oldEnumClass);
            if (oldEnums.length == 0) {
                return SuggestionResult.empty();
            }

            return Arrays.stream(oldEnums)
                .map(oldEnum -> OldEnumAccessor.invokeName(oldEnum))
                .collect(SuggestionResult.collector());
        });
    }

}
