package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotations.command.ParameterPreparedArgument;
import dev.rollczi.litecommands.argument.ArgumentParser;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.wrapper.WrapperFormat;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.function.BiFunction;

class ArgPreparedArgument<SENDER, EXPECTED> implements ParameterPreparedArgument<SENDER, EXPECTED> {

    private final ParameterArgument<?, EXPECTED> argument;
    private final ArgumentParser<SENDER, ?, ?> resolver;
    private final BiFunction<Invocation<SENDER>, List<String>, ArgumentResult<EXPECTED>> parser;

    ArgPreparedArgument(
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

}
