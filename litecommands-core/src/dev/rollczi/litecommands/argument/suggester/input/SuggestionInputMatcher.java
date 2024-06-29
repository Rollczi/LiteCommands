package dev.rollczi.litecommands.argument.suggester.input;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.input.InputMatcher;
import dev.rollczi.litecommands.argument.suggester.Suggester;

public interface SuggestionInputMatcher<SELF extends SuggestionInputMatcher<SELF>> extends InputMatcher {

    @Override
    boolean hasNextRoute();

    @Override
    String nextRoute();

    @Override
    String showNextRoute();

    <SENDER, T> boolean isOptionalArgument(
        Invocation<SENDER> invocation,
        Argument<T> argument,
        Parser<SENDER, T> parserSet
    );

    <SENDER, T> SuggestionInputResult nextArgument(
        Invocation<SENDER> invocation,
        Argument<T> argument,
        Parser<SENDER, T> parser,
        Suggester<SENDER, T> suggesterSet
    );

    SELF copy();

    boolean nextRouteIsLast();

    boolean hasNoNextRouteAndArguments();

}
