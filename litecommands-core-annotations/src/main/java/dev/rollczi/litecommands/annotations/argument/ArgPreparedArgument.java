package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotations.command.ParameterPreparedArgument;
import dev.rollczi.litecommands.argument.ArgumentParser;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.argument.PreparedArgumentResult;
import dev.rollczi.litecommands.argument.SuccessfulResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.wrapper.WrappedExpectedFactory;
import dev.rollczi.litecommands.wrapper.WrapperFormat;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.function.BiFunction;

class ArgPreparedArgument<SENDER, EXPECTED> implements ParameterPreparedArgument<SENDER, EXPECTED> {

    private final ParameterArgument<?, EXPECTED> argument;
    private final ArgumentParser<SENDER, ?, ?> resolver;
    private final BiFunction<Invocation<SENDER>, List<String>, ArgumentResult<EXPECTED>> parser;
    private final WrappedExpectedFactory wrappedExpectedFactory;

    ArgPreparedArgument(
        ParameterArgument<?, EXPECTED> argument,
        ArgumentParser<SENDER, EXPECTED, ?> resolver,
        BiFunction<Invocation<SENDER>, List<String>, ArgumentResult<EXPECTED>> parser,
        WrappedExpectedFactory wrappedExpectedFactory
    ) {
        this.argument = argument;
        this.resolver = resolver;
        this.parser = parser;
        this.wrappedExpectedFactory = wrappedExpectedFactory;
    }

    @Override
    public PreparedArgumentResult<EXPECTED> resolve(Invocation<SENDER> invocation, List<String> arguments) {
        if (!resolver.getRange().isInRange(arguments.size()) && wrappedExpectedFactory.canCreateEmpty()) {
            return PreparedArgumentResult.success(() -> wrappedExpectedFactory.createEmpty(this.getWrapperFormat()), 0);
        }

        ArgumentResult<EXPECTED> result = parser.apply(invocation, arguments);

        if (result.isSuccessful()) {
            SuccessfulResult<EXPECTED> successfulResult = result.getSuccessfulResult();

            return PreparedArgumentResult.success(() -> wrappedExpectedFactory.create(successfulResult.getExpectedProvider(), this.getWrapperFormat()), successfulResult.getConsumedRawArguments());
        }

        return PreparedArgumentResult.failed(result.getFailedReason());
    }

    @Override
    public Range getRange() {
        if (wrappedExpectedFactory.canCreateEmpty()) {
            return Range.range(0, resolver.getRange().getMax());
        }

        return resolver.getRange();
    }

    @Override
    public WrapperFormat<EXPECTED, ?> getWrapperFormat() {
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
