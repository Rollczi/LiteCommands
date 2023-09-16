package dev.rollczi.litecommands.suggestion;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.suggester.Suggester;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.argument.suggester.input.SuggestionInputMatcher;
import dev.rollczi.litecommands.argument.suggester.input.SuggestionInputResult;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.parser.ParserSet;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.requirement.ArgumentRequirement;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.validator.ValidatorService;

public class SuggestionService<SENDER> {

    private final ParserRegistry<SENDER> parserRegistry;
    private final SuggesterRegistry<SENDER> suggesterRegistry;
    private final ValidatorService<SENDER> validatorService;

    public SuggestionService(ParserRegistry<SENDER> parserRegistry, SuggesterRegistry<SENDER> suggesterRegistry, ValidatorService<SENDER> validatorService) {
        this.parserRegistry = parserRegistry;
        this.suggesterRegistry = suggesterRegistry;
        this.validatorService = validatorService;
    }

    public SuggestionResult suggest(
        Invocation<SENDER> invocation,
        SuggestionInputMatcher<?> matcher,
        CommandRoute<SENDER> commandRoute
    ) {
        return this.suggest0(invocation, matcher, commandRoute);
    }

    private <MATCHER extends SuggestionInputMatcher<MATCHER>> SuggestionResult suggest0(
        Invocation<SENDER> invocation,
        SuggestionInputMatcher<MATCHER> matcher,
        CommandRoute<SENDER> commandRoute
    ) {
        if (matcher.hasNoNextRouteAndArguments()) {
            return SuggestionResult.of(commandRoute.names());
        }

        SuggestionResult all = SuggestionResult.empty();

        for (CommandExecutor<SENDER, ?> executor : commandRoute.getExecutors()) {
            Flow flow = this.validatorService.validate(invocation, executor);

            if (flow.isTerminate() || flow.isStopCurrent()) {
                continue;
            }

            SuggestionResult result = this.suggestExecutor(invocation, matcher.copy(), executor);

            all.addAll(result.getSuggestions());
        }

        if (!matcher.nextRouteIsLast()) {
            return all;
        }

        String current = matcher.showNextRoute();

        for (CommandRoute<SENDER> child : commandRoute.getChildren()) {
            for (String name : child.names()) {
                if (!name.startsWith(current)) {
                    continue;
                }

                all.add(Suggestion.of(name));
            }
        }

        return all;
    }

    public <MATCHER extends SuggestionInputMatcher<MATCHER>> SuggestionResult suggestExecutor(
        Invocation<SENDER> invocation,
        MATCHER matcher,
        CommandExecutor<SENDER, ?> executor
    ) {
        SuggestionResult collector = SuggestionResult.empty();

        for (Requirement<SENDER, ?> requirement : executor.getRequirements()) {
            if (!(requirement instanceof ArgumentRequirement)) {
                continue;
            }

            SuggestionInputResult result = suggestRequirement(invocation, matcher, (ArgumentRequirement<SENDER, ?>) requirement);
            collector.addAll(result.getResult());

            switch (result.getCause()) {
                case CONTINUE: continue;
                case END: return collector;
                case FAIL: return SuggestionResult.empty();
            }
        }

        return collector;
    }

    private <OUT, MATCHER extends SuggestionInputMatcher<MATCHER>> SuggestionInputResult suggestRequirement(
        Invocation<SENDER> invocation,
        MATCHER matcher,
        ArgumentRequirement<SENDER, OUT> requirement
    ) {
        Argument<?> argument = requirement.getArgument();

        return suggestArgument(invocation, matcher, argument);
    }

    private <PARSED, MATCHER extends SuggestionInputMatcher<MATCHER>> SuggestionInputResult suggestArgument(
        Invocation<SENDER> invocation,
        MATCHER matcher,
        Argument<PARSED> argument
    ) {
        Class<PARSED> parsedType = argument.getWrapperFormat().getParsedType();
        ParserSet<SENDER, PARSED> parserSet = parserRegistry.getParserSet(parsedType, argument.toKey());
        Suggester<SENDER, PARSED> suggester = suggesterRegistry.getSuggester(parsedType, argument.toKey());

        boolean nextOptional = matcher.isNextOptional(argument, parserSet);
        SuggestionInputResult result = matcher.nextArgument(invocation, argument, parserSet, suggester);

        if (result.isEnd() && nextOptional) {
            return SuggestionInputResult.continueWith(result);
        }

        return result;
    }

}
