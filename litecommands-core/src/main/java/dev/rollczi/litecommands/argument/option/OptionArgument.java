package dev.rollczi.litecommands.argument.option;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentContext;
import dev.rollczi.litecommands.argument.ParameterHandler;
import dev.rollczi.litecommands.argument.simple.MultilevelArgument;
import dev.rollczi.litecommands.command.sugesstion.Suggestion;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;
import panda.std.Blank;
import panda.std.Option;
import panda.std.Result;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OptionArgument<SENDER, T> implements Argument<SENDER, Opt>, ParameterHandler {

    private final Class<T> type;
    private final MultilevelArgument<T> multilevel;

    public OptionArgument(Class<T> type, MultilevelArgument<T> multilevel) {
        this.type = type;
        this.multilevel = multilevel;
    }

    @Override
    public MatchResult match(LiteInvocation invocation, ArgumentContext<Opt> context) {
        int currentArgument = context.currentArgument();
        Option<Class<?>> optionType = OptionUtils.extractOptionType(context.parameter());

        if (optionType.isEmpty() || !optionType.get().equals(type)) {
            throw new IllegalStateException();
        }

        List<String> arguments = Arrays.asList(invocation.arguments());

        if (currentArgument + multilevel.countMultilevel() > arguments.size()) {
            return MatchResult.notMatched();
        }

        List<String> argumentsToParse = arguments.subList(currentArgument, currentArgument + multilevel.countMultilevel());
        Result<T, ?> parsed = this.multilevel.parseMultilevel(invocation, argumentsToParse.toArray(new String[0]));

        if (parsed.isErr()) {
            Object error = parsed.getError();

            if (error instanceof Blank) {
                return MatchResult.notMatched();
            }

            return MatchResult.notMatched(error);
        }

        return MatchResult.matched(parsed.toOption(), multilevel.countMultilevel());
    }

    @Override
    public List<Suggestion> suggestion(LiteInvocation invocation, Parameter parameter, Opt annotation) {
        return this.multilevel.suggest(invocation);
    }

    @Override
    public Class<?> getNativeClass() {
        return this.multilevel.getClass();
    }

    @Override
    public boolean isOptional() {
        return true;
    }

    @Override
    public List<Object> defaultValue() {
        return Collections.singletonList(Option.none());
    }

    @Override
    public boolean canHandle(Class<?> type, Parameter parameter) {
        return OptionUtils.extractOptionType(parameter)
                .map(type::equals)
                .orElseGet(false);
    }

    @Override
    public boolean canHandleAssignableFrom(Class<?> type, Parameter parameter) {
        return OptionUtils.extractOptionType(parameter)
                .map(paramType -> paramType.isAssignableFrom(type))
                .orElseGet(false);
    }

}
