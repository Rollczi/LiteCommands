package dev.rollczi.litecommands.modern.annotation.command;

import dev.rollczi.litecommands.modern.annotation.argument.ParameterArgument;
import dev.rollczi.litecommands.modern.argument.Argument;
import dev.rollczi.litecommands.modern.argument.ArgumentParser;
import dev.rollczi.litecommands.modern.argument.ArgumentResult;
import dev.rollczi.litecommands.modern.argument.PreparedArgument;
import dev.rollczi.litecommands.modern.argument.PreparedArgumentImpl;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.range.Range;
import dev.rollczi.litecommands.modern.wrapper.WrapperFormat;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.function.BiFunction;

class ArgPreparedArgument<SENDER, EXPECTED> implements ParameterPreparedArgument<SENDER, EXPECTED> {

    private final ParameterArgument<?, EXPECTED> argument;
    private final ArgumentParser<SENDER, ?, ?> resolver;
    private final BiFunction<Invocation<SENDER>, List<String>, ArgumentResult<EXPECTED>> parser;

    protected ArgPreparedArgument(
        ParameterArgument<?, EXPECTED> argument,
        ArgumentParser<SENDER, EXPECTED, ?> resolver,
        BiFunction<Invocation<SENDER>, List<String>, ArgumentResult<EXPECTED>> parser
    ) {
        this.argument = argument;
        this.resolver = resolver;
        this.parser = parser;
    }

    @Override
    public ArgumentResult<EXPECTED> resolve(Invocation<SENDER> invocation, List<String> arguments) {
        return parser.apply(invocation, arguments);
    }

    @Override
    public Range getRange() {
        return resolver.getRange();
    }

    @Override
    public WrapperFormat<EXPECTED> getWrapperFormat() {
        return argument.getWrapperFormat();
    }

    @Override
    public Parameter getParameter() {
        return argument.getParameter();
    }

    @Override
    public int getParameterIndex() {
        return argument.getParameterIndex();
    }

    public static <SENDER, E, A extends ParameterArgument<?, E>> ArgPreparedArgument<SENDER, E> create(A argument, ArgumentParser<SENDER, E, A> resolver) {
        return new ArgPreparedArgument<>(argument, resolver, (invocation, arguments) -> resolver.parse(invocation, argument, arguments));
    }

}
