package dev.rollczi.litecommands.suggestion;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.suggester.Suggester;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.argument.suggester.input.SuggestionInputMatcher;
import dev.rollczi.litecommands.argument.suggester.input.SuggestionInputResult;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.event.EventPublisher;
import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.event.SuggestionCommandRouteEvent;
import dev.rollczi.litecommands.suggestion.event.SuggestionExecutorEvent;
import dev.rollczi.litecommands.util.StringUtil;
import dev.rollczi.litecommands.validator.ValidatorService;

public class SuggestionService<SENDER> {

    private final ParserRegistry<SENDER> parserRegistry;
    private final SuggesterRegistry<SENDER> suggesterRegistry;
    private final EventPublisher publisher;

    public SuggestionService(ParserRegistry<SENDER> parserRegistry, SuggesterRegistry<SENDER> suggesterRegistry, ValidatorService<SENDER> validatorService, EventPublisher publisher) {
        this.parserRegistry = parserRegistry;
        this.suggesterRegistry = suggesterRegistry;
        this.publisher = publisher;
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
            SuggestionCommandRouteEvent event = this.publisher.publish(new SuggestionCommandRouteEvent(invocation, commandRoute));

            if (event.isCancelled()) {
                return SuggestionResult.empty();
            }

            return SuggestionResult.of(commandRoute.names());
        }

        SuggestionResult all = SuggestionResult.empty();

        for (CommandExecutor<SENDER> executor : commandRoute.getExecutors()) {
            SuggestionExecutorEvent executorEvent = this.publisher.publish(new SuggestionExecutorEvent(invocation, executor));

            if (executorEvent.isCancelled()) {
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
            if (!this.isAnyExecutorValid(invocation, child)) {
                continue;
            }

            for (String name : child.names()) {
                if (!StringUtil.startsWithIgnoreCase(name, current)) {
                    continue;
                }

                all.add(Suggestion.of(name));
            }
        }

        return all;
    }

    private boolean isAnyExecutorValid(Invocation<SENDER> invocation, CommandRoute<SENDER> route) {
        SuggestionCommandRouteEvent event = this.publisher.publish(new SuggestionCommandRouteEvent(invocation, route));

        if (event.isCancelled()) {
            return false;
        }

        for (CommandExecutor<SENDER> executor : route.getExecutors()) {
            SuggestionExecutorEvent executorEvent = this.publisher.publish(new SuggestionExecutorEvent(invocation, executor));

            if (executorEvent.isCancelled()) {
                continue;
            }

            return true;
        }

        for (CommandRoute<SENDER> child : route.getChildren()) {
            if (this.isAnyExecutorValid(invocation, child)) {
                return true;
            }
        }

        return false;
    }

    public <MATCHER extends SuggestionInputMatcher<MATCHER>> SuggestionResult suggestExecutor(
        Invocation<SENDER> invocation,
        MATCHER matcher,
        CommandExecutor<SENDER> executor
    ) {
        SuggestionResult collector = SuggestionResult.empty();

        for (Argument<?> argument : executor.getArguments()) {
            SuggestionInputResult result = suggestArgument(invocation, matcher, argument);
            collector.addAll(result.getResult());

            switch (result.getCause()) {
                case CONTINUE: continue;
                case END: return collector;
                case FAIL: return SuggestionResult.empty();
            }
        }

        return collector;
    }

    private <PARSED, MATCHER extends SuggestionInputMatcher<MATCHER>> SuggestionInputResult suggestArgument(
        Invocation<SENDER> invocation,
        MATCHER matcher,
        Argument<PARSED> argument
    ) {
        Class<PARSED> parsedType = argument.getType().getRawType();
        Parser<SENDER, PARSED> parser = parserRegistry.getParser(argument);
        Suggester<SENDER, PARSED> suggester = suggesterRegistry.getSuggester(parsedType, argument.getKey());

        SuggestionInputResult result = matcher.nextArgument(invocation, argument, parser, suggester);

        if (result.isEnd() && matcher.isOptionalArgument(invocation, argument, parser)) {
            return SuggestionInputResult.continueWith(result);
        }

        return result;
    }

}
