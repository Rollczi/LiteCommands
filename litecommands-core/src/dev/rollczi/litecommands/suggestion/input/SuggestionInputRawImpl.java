package dev.rollczi.litecommands.suggestion.input;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.argument.input.RawInputAnalyzer;
import dev.rollczi.litecommands.argument.parser.ArgumentParserSet;
import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.Suggester;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import panda.std.Blank;

import java.util.Collections;
import java.util.List;

public class SuggestionInputRawImpl implements SuggestionInput<SuggestionInputRawImpl.Matcher> {

    private final List<String> rawArguments;

    SuggestionInputRawImpl(List<String> rawArguments) {
        this.rawArguments = rawArguments;
    }

    @Override
    public Matcher createMatcher() {
        return new Matcher();
    }

    @Override
    public List<String> asList() {
        return Collections.unmodifiableList(this.rawArguments);
    }

    public class Matcher implements SuggestionInputMatcher<Matcher> {

        private final RawInputAnalyzer rawInputAnalyzer = new RawInputAnalyzer(rawArguments);

        public Matcher() {}

        public Matcher(int pivotPosition) {
            this.rawInputAnalyzer.setPivotPosition(pivotPosition);
        }

        @Override
        public boolean hasNextRoute() {
            return rawInputAnalyzer.hasNextRoute();
        }

        @Override
        public boolean nextRouteIsLast() {
            return rawInputAnalyzer.nextRouteIsLast();
        }

        @Override
        public boolean hasNoNextRouteAndArguments() {
            return !rawInputAnalyzer.hasNextRoute();
        }

        @Override
        public String nextRoute() {
            return rawInputAnalyzer.nextRoute();
        }

        @Override
        public String showNextRoute() {
            return rawInputAnalyzer.showNextRoute();
        }

        @Override
        public <SENDER, T> Flow nextArgument(
            Invocation<SENDER> invocation,
            Argument<T> argument,
            ArgumentParserSet<SENDER, T> parserSet,
            Suggester<SENDER, T> suggester
        ) {
            RawInputAnalyzer.Context<SENDER, T> context = rawInputAnalyzer.toContext(argument, parserSet);

            if (context.isMissingFullArgument()) {
                Suggestion current = Suggestion.of(rawInputAnalyzer.getLastArgument());
                SuggestionContext suggestionContext = new SuggestionContext(current);
                SuggestionResult result = suggester.suggest(invocation, argument, suggestionContext)
                    .filterBy(current);

                return Flow.stopCurrentFlow(result);
            }

            if (context.isMissingPartOfArgument() || context.isLastRawArgument()) {
                Suggestion current = Suggestion.from(context.getAllNotConsumedArguments());
                SuggestionContext suggestionContext = new SuggestionContext(current);
                SuggestionResult result = suggester.suggest(invocation, argument, suggestionContext)
                    .filterBy(current);

                return Flow.stopCurrentFlow(result);
            }

            ArgumentResult<T> result = context.parseArgument(invocation);

            if (result.isFailed()) {
                return Flow.terminateFlow(result);
            }

            return Flow.continueFlow();
        }

        @Override
        public Matcher copy() {
            return new Matcher(rawInputAnalyzer.getPivotPosition());
        }

    }

}
