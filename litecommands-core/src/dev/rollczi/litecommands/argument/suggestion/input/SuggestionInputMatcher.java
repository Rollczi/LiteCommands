package dev.rollczi.litecommands.argument.suggestion.input;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParserSet;
import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.input.InputMatcher;
import dev.rollczi.litecommands.argument.suggestion.Suggester;

public interface SuggestionInputMatcher<SELF extends SuggestionInputMatcher<SELF>> extends InputMatcher {

    @Override
    boolean hasNextRoute();

    @Override
    String nextRoute();

    @Override
    String showNextRoute();

    <SENDER, T> Flow nextArgument(
        Invocation<SENDER> invocation,
        Argument<T> argument,
        ParserSet<SENDER, T> parserSet,
        Suggester<SENDER, T> suggesterSet
    );

    SELF copy();

    boolean nextRouteIsLast();

    boolean hasNoNextRouteAndArguments();

}
