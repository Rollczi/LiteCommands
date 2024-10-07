package dev.rollczi.litecommands.argument.resolver.completeable;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.ParserChainAccessor;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolverChained;
import dev.rollczi.litecommands.argument.suggester.SuggesterChainAccessor;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.scheduler.SchedulerPoll;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import java.util.concurrent.CompletableFuture;

public class CompletableFutureResolver<SENDER> extends ArgumentResolverChained<SENDER, CompletableFuture> {

    private final Scheduler scheduler;
    private final ParserRegistry<SENDER> parserRegistry;

    public CompletableFutureResolver(Scheduler scheduler, ParserRegistry<SENDER> parserRegistry) {
        this.scheduler = scheduler;
        this.parserRegistry = parserRegistry;
    }

    @Override
    public boolean canParse(Argument<CompletableFuture> argument) {
        return canParse(argument, argument.getType().getParameterized());
    }

    private <T> boolean canParse(Argument<CompletableFuture> argument, TypeToken<T> type) {
        Parser<SENDER, T> parser = parserRegistry.getParser(argument.createChild(type));

        return parser.canParse(argument.createChild(type));
    }

    @Override
    protected ParseResult<CompletableFuture> parse(Invocation<SENDER> invocation, Argument<CompletableFuture> argument, String input, ParserChainAccessor<SENDER> chainAccessor) {
        return this.parse(invocation, argument, argument.getType().getParameterized(), input, chainAccessor);
    }

    private <T> ParseResult<CompletableFuture> parse(Invocation<SENDER> invocation, Argument<CompletableFuture> argument, TypeToken<T> type, String input, ParserChainAccessor<SENDER> chainAccessor) {
        CompletableFuture<ParseResult<T>> supply = scheduler.supply(SchedulerPoll.COMPLETABLE_FUTURE, () -> chainAccessor.parse(invocation, argument.createChild(type), input));

        return ParseResult.completableFuture(supply)
            .map(future -> CompletableFuture.completedFuture(future));
    }

    @Override
    protected boolean match(Invocation<SENDER> invocation, Argument<CompletableFuture> context, String argument, ParserChainAccessor<SENDER> accessor) {
        return match(invocation, context, argument, accessor, context.getType().getParameterized());
    }

    private <T> boolean match(Invocation<SENDER> invocation, Argument<CompletableFuture> context, String argument, ParserChainAccessor<SENDER> accessor, TypeToken<T> type) {
        Parser<SENDER, T> parser = parserRegistry.getParser(context.createChild(type));

        return parser.match(invocation, context.createChild(type), RawInput.of(argument));
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<CompletableFuture> argument, SuggestionContext context, SuggesterChainAccessor<SENDER> chainAccessor) {
        return chainAccessor.suggest(invocation, argument, context);
    }

}
