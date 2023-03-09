package dev.rollczi.litecommands.modern.annotation.argument;

import dev.rollczi.litecommands.modern.argument.ArgumentParser;
import dev.rollczi.litecommands.modern.argument.ArgumentResult;
import dev.rollczi.litecommands.modern.argument.PreparedArgumentImpl;
import dev.rollczi.litecommands.modern.invocation.Invocation;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.function.BiFunction;

public class PreparedParameterArgumentImpl<SENDER, EXPECTED> extends PreparedArgumentImpl<SENDER, EXPECTED> {

    private final ParameterArgument<?, EXPECTED> argument;

    protected PreparedParameterArgumentImpl(ParameterArgument<?, EXPECTED> argument, ArgumentParser<SENDER, EXPECTED, ?> resolver, BiFunction<Invocation<SENDER>, List<String>, ArgumentResult<EXPECTED>> parser) {
        super(argument, resolver, parser);
        this.argument = argument;
    }

    public int getParameterIndex() {
        return argument.getParameterIndex();
    }

    public static <S, D extends Annotation, E, A extends ParameterArgument<D, E>> PreparedParameterArgumentImpl<S, E> create(A argument, ArgumentParser<S, E, A> resolver) {
        return new PreparedParameterArgumentImpl<>(argument, resolver, (invocation, arguments) -> resolver.parse(invocation, argument, arguments));
    }

}
