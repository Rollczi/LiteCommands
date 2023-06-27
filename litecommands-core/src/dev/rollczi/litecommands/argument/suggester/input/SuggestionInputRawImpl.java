package dev.rollczi.litecommands.argument.suggester.input;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.suggester.Suggester;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.input.raw.RawInputAnalyzer;
import dev.rollczi.litecommands.argument.parser.ParserSet;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

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
        public <SENDER, T> boolean isNextOptional(Argument<T> argument, ParserSet<SENDER, T> parserSet) {
            return rawInputAnalyzer.isNextOptional(parserSet, argument);
        }

        @Override
        public <SENDER, T> SuggestionInputResult nextArgument(
            Invocation<SENDER> invocation,
            Argument<T> argument,
            ParserSet<SENDER, T> parserSet,
            Suggester<SENDER, T> suggester
        ) {
            RawInputAnalyzer.Context<SENDER, T> context = rawInputAnalyzer.toContext(argument, parserSet);

            if (context.isMissingFullArgument()) {
                Suggestion current = Suggestion.of(rawInputAnalyzer.getLastArgument());
                SuggestionContext suggestionContext = new SuggestionContext(current);
                SuggestionResult result = suggester.suggest(invocation, argument, suggestionContext)
                    .filterBy(current);

                return SuggestionInputResult.endWith(result);
            }

            if (context.isMissingPartOfArgument() || context.isLastRawArgument()) {
                Suggestion current = Suggestion.from(context.getAllNotConsumedArguments());
                SuggestionContext suggestionContext = new SuggestionContext(current);
                SuggestionResult result = suggester.suggest(invocation, argument, suggestionContext)
                    .filterBy(current);

                return SuggestionInputResult.endWith(result);
            }

            ParseResult<T> result = context.parseArgument(invocation);

            if (result.isFailed()) {
                return SuggestionInputResult.fail();
            }

            return SuggestionInputResult.continueWithout();
        }

        @Override
        public Matcher copy() {
            return new Matcher(rawInputAnalyzer.getPivotPosition());
        }

    }

}
