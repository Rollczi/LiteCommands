package dev.rollczi.litecommands.argument.enumeration;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.ArgumentContext;
import dev.rollczi.litecommands.argument.ParameterHandler;
import dev.rollczi.litecommands.argument.SingleArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;
import dev.rollczi.litecommands.shared.EnumUtil;
import dev.rollczi.litecommands.suggestion.Suggestion;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EnumArgument<SENDER> implements SingleArgument<SENDER, Arg>, ParameterHandler {

    private final Map<Class<?>, List<Suggestion>> cachedEnumSuggestions = new HashMap<>();

    @Override
    public MatchResult match(LiteInvocation invocation, ArgumentContext<Arg> context, String argument) {
        return EnumUtil.parse(context.parameter().getType(), argument)
                .fold(MatchResult::matchedSingle, exception -> MatchResult.notMatched());
    }

    @Override
    public List<Suggestion> suggestion(LiteInvocation invocation, Parameter parameter, Arg annotation) {
        return cachedEnumSuggestions.computeIfAbsent(parameter.getType(), this::createSuggestions);
    }

    private List<Suggestion> createSuggestions(Class<?> typeOfEnum) {
        Object[] enumConstants = typeOfEnum.getEnumConstants();

        if (enumConstants == null || enumConstants.length == 0) {
            return Collections.emptyList();
        }

        return Arrays.stream(enumConstants)
                .map(Object::toString)
                .map(Suggestion::of)
                .collect(Collectors.toList());
    }

    @Override
    public boolean canHandleAssignableFrom(Class<?> type, Parameter parameter) {
        return Enum.class.isAssignableFrom(parameter.getType());
    }

}
