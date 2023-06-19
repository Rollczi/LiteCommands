package dev.rollczi.litecommands.suggestion;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.FailedReason;
import dev.rollczi.litecommands.argument.parser.ArgumentParserRegistry;
import dev.rollczi.litecommands.argument.parser.ArgumentParserSet;
import dev.rollczi.litecommands.command.CommandExecutor;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.requirement.ArgumentRequirement;
import dev.rollczi.litecommands.command.requirement.Requirement;
import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.input.SuggestionInputMatcher;
import dev.rollczi.litecommands.validator.ValidatorService;

public class SuggestionService<SENDER> {

    private final ArgumentParserRegistry<SENDER> argumentParserRegistry;
    private final SuggesterRegistry<SENDER> suggesterRegistry;
    private final ValidatorService<SENDER> validatorService;

    public SuggestionService(ArgumentParserRegistry<SENDER> argumentParserRegistry, SuggesterRegistry<SENDER> suggesterRegistry, ValidatorService<SENDER> validatorService) {
        this.argumentParserRegistry = argumentParserRegistry;
        this.suggesterRegistry = suggesterRegistry;
        this.validatorService = validatorService;
    }

    public <MATCHER extends SuggestionInputMatcher<MATCHER>> SuggestionResult suggest(
        Invocation<SENDER> invocation,
        MATCHER matcher,
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
        ArgumentParserSet<SENDER, PARSED> parserSet = argumentParserRegistry.getParserSet(parsedType, argument.toKey());
        Suggester<SENDER, PARSED> suggester = suggesterRegistry.getSuggester(parsedType, argument.toKey());

        return matcher.nextArgument(invocation, argument, parserSet, suggester);
    }

}
