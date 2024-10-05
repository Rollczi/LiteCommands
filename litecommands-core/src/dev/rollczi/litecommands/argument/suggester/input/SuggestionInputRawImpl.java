package dev.rollczi.litecommands.argument.suggester.input;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.suggester.Suggester;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.input.raw.RawInputAnalyzer;
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
        public <SENDER, T> boolean isOptionalArgument(Invocation<SENDER> invocation, Argument<T> argument, Parser<SENDER, T> parser) {
            Range range = parser.getRange(argument);

            if (range.getMin() == 0) {
                return true;
            }

            return argument.hasDefaultValue();
        }

        @Override
        public <SENDER, T> SuggestionInputResult nextArgument(
            Invocation<SENDER> invocation,
            Argument<T> argument,
            Parser<SENDER, T> parser,
            Suggester<SENDER, T> suggester
        ) {
            RawInputAnalyzer.Context<SENDER, T> context = rawInputAnalyzer.toContext(argument, parser);

            if (context.isMissingFullArgument()) {
                Suggestion current = Suggestion.from(rawInputAnalyzer.getLastArgumentsBeforePivotMove());
                SuggestionContext suggestionContext = new SuggestionContext(current);
                SuggestionResult result = suggester.suggest(invocation, argument, suggestionContext)
                    .filterBy(current);

                return SuggestionInputResult.endWith(result);
            }

            if (context.isMissingPartOfArgument()) {
                Suggestion current = Suggestion.from(context.getAllNotConsumedArguments());
                SuggestionContext suggestionContext = new SuggestionContext(current);
                SuggestionResult result = suggester.suggest(invocation, argument, suggestionContext)
                    .filterBy(current);

                return SuggestionInputResult.endWith(result);
            }

            if (context.isLastArgument()) {
                Suggestion current = Suggestion.from(context.getAllNotConsumedArguments());
                SuggestionContext suggestionContext = new SuggestionContext(current);
                SuggestionResult result = suggester.suggest(invocation, argument, suggestionContext)
                    .filterBy(current);

                int consumed = suggestionContext.getConsumed();
                if (consumed == current.lengthMultilevel()) {
                    if (isOptionalArgument(invocation, argument, parser)) {
                        rawInputAnalyzer.setLastOptionalArgument(true);
                        return SuggestionInputResult.endWith(result);
                    }

                    rawInputAnalyzer.consumeAll();
                    return SuggestionInputResult.endWith(result);
                }
            }

            boolean isMatch = context.matchArgument(invocation);

            if (isMatch) {
                return SuggestionInputResult.continueWithout();
            }

            return SuggestionInputResult.fail();
        }

        @Override
        public Matcher copy() {
            return new Matcher(rawInputAnalyzer.getPivotPosition());
        }

    }

}
