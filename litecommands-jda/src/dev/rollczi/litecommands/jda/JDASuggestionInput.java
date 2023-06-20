package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParserSet;
import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.argument.suggestion.Suggester;
import dev.rollczi.litecommands.argument.suggestion.Suggestion;
import dev.rollczi.litecommands.argument.suggestion.SuggestionContext;
import dev.rollczi.litecommands.argument.suggestion.SuggestionResult;
import dev.rollczi.litecommands.argument.suggestion.input.SuggestionInput;
import dev.rollczi.litecommands.argument.suggestion.input.SuggestionInputMatcher;
import net.dv8tion.jda.api.interactions.AutoCompleteQuery;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.List;
import java.util.Map;

class JDASuggestionInput extends AbstractJDAInput<JDASuggestionInput.JDASuggestionMatcher> implements SuggestionInput<JDASuggestionInput.JDASuggestionMatcher> {

    private final AutoCompleteQuery currentOption;

    protected JDASuggestionInput(List<String> routes, Map<String, OptionMapping> arguments, AutoCompleteQuery currentOption) {
        super(routes, arguments);
        this.currentOption = currentOption;
    }

    @Override
    public JDASuggestionMatcher createMatcher() {
        return new JDASuggestionMatcher();
    }

    class JDASuggestionMatcher extends AbstractJDAInput<JDASuggestionMatcher>.AbstractJDAMatcher implements SuggestionInputMatcher<JDASuggestionMatcher> {

        JDASuggestionMatcher() {}

        JDASuggestionMatcher(int routePosition) {
            super(routePosition);
        }

        @Override
        public <SENDER, T> Flow nextArgument(Invocation<SENDER> invocation, Argument<T> argument, ParserSet<SENDER, T> parserSet, Suggester<SENDER, T> suggesterSet) {
            if (!argument.getName().equals(currentOption.getName())) {
                return Flow.continueFlow();
            }

            String current = currentOption.getValue();
            Suggestion suggestion = Suggestion.of(current);
            SuggestionContext context = new SuggestionContext(suggestion);
            SuggestionResult result = suggesterSet.suggest(invocation, argument, context);

            return Flow.stopCurrentFlow(result);
        }

        @Override
        public JDASuggestionMatcher copy() {
            return new JDASuggestionMatcher(routePosition);
        }

        @Override
        public boolean nextRouteIsLast() {
            return false; // don't suggest routes
        }

        @Override
        public boolean hasNoNextRouteAndArguments() {
            return !hasNextRoute() && arguments.isEmpty();
        }

    }

}
