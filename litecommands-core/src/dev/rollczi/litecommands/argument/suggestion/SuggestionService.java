package dev.rollczi.litecommands.argument.suggestion;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.suggestion.input.SuggestionInputMatcher;
import dev.rollczi.litecommands.shared.FailedReason;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.parser.ParserSet;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.argument.ArgumentRequirement;
import dev.rollczi.litecommands.command.requirement.Requirement;
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
            Flow flow = this.validatorService.validate(invocation, commandRoute, executor);

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

    @SuppressWarnings("unchecked")
    public <MATCHER extends SuggestionInputMatcher<MATCHER>> SuggestionResult suggestExecutor(
        Invocation<SENDER> invocation,
        MATCHER matcher,
        CommandExecutor<SENDER, ?> executor
    ) {
        for (Requirement<SENDER, ?> requirement : executor.getRequirements()) {
            if (!(requirement instanceof ArgumentRequirement)) {
                continue;
            }

            Flow flow = suggestRequirement(invocation, matcher, (ArgumentRequirement<SENDER, ?>) requirement);

            switch (flow.status()) {
                case CONTINUE: continue;
                case TERMINATE: return SuggestionResult.empty();
                case STOP_CURRENT: {
                    FailedReason failedReason = flow.failedReason();
                    Object reason = failedReason.getReasonOr(null);

                    if (reason instanceof SuggestionResult) {
                        return (SuggestionResult) reason;
                    }

                    return SuggestionResult.empty();
                }
            }
        }

        return SuggestionResult.empty();
    }

    private <OUT, MATCHER extends SuggestionInputMatcher<MATCHER>> Flow suggestRequirement(
        Invocation<SENDER> invocation,
        MATCHER matcher,
        ArgumentRequirement<SENDER, OUT> requirement
    ) {
        Argument<?> argument = requirement.getArgument();

        return suggestArgument(invocation, matcher, argument);
    }

    private <PARSED, MATCHER extends SuggestionInputMatcher<MATCHER>> Flow suggestArgument(
        Invocation<SENDER> invocation,
        MATCHER matcher,
        Argument<PARSED> argument
    ) {
        Class<PARSED> parsedType = argument.getWrapperFormat().getParsedType();
        ParserSet<SENDER, PARSED> parserSet = parserRegistry.getParserSet(parsedType, argument.toKey());
        Suggester<SENDER, PARSED> suggester = suggesterRegistry.getSuggester(parsedType, argument.toKey());

        return matcher.nextArgument(invocation, argument, parserSet, suggester);
    }

}
