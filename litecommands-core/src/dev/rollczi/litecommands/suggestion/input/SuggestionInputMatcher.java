package dev.rollczi.litecommands.suggestion.input;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ArgumentParserSet;
import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.command.input.InputMatcher;
import dev.rollczi.litecommands.suggestion.Suggester;

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
        ArgumentParserSet<SENDER, T> parserSet,
        Suggester<SENDER, T> suggesterSet
    );

    SELF copy();

    boolean nextRouteIsLast();

    boolean hasNoNextRouteAndArguments();

}
