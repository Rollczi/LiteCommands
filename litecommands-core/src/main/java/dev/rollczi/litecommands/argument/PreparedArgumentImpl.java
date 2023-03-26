package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.wrapper.WrapperFormat;

import java.util.List;
import java.util.function.BiFunction;

public class PreparedArgumentImpl<SENDER, EXPECTED> implements PreparedArgument<SENDER, EXPECTED> {

    private final Argument<EXPECTED> argument;
    private final ArgumentParser<SENDER, ?, ?> resolver;
    private final BiFunction<Invocation<SENDER>, List<String>, ArgumentResult<EXPECTED>> parser;

    protected PreparedArgumentImpl(Argument<EXPECTED> argument, ArgumentParser<SENDER, EXPECTED, ?> resolver, BiFunction<Invocation<SENDER>, List<String>, ArgumentResult<EXPECTED>> parser) {
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

    public static <SENDER, E, A extends Argument<E>> PreparedArgumentImpl<SENDER, E> create(A argument, ArgumentParser<SENDER, E, A> resolver) {
        return new PreparedArgumentImpl<>(argument, resolver, (invocation, arguments) -> resolver.parse(invocation, argument, arguments));
    }

}
