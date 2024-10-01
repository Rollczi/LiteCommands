package dev.rollczi.litecommands.requirement;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.parser.ParserSet;
import dev.rollczi.litecommands.argument.parser.input.ParseableInputMatcher;
import dev.rollczi.litecommands.bind.BindRegistry;
import dev.rollczi.litecommands.bind.BindRequirement;
import dev.rollczi.litecommands.bind.BindResult;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.context.ContextRegistry;
import dev.rollczi.litecommands.context.ContextRequirement;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.shared.BiHashMap;
import dev.rollczi.litecommands.shared.BiMap;
import dev.rollczi.litecommands.shared.ThrowingSupplier;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

class ScheduledRequirementResolver<SENDER> {

    private final ContextRegistry<SENDER> contextRegistry;
    private final ParserRegistry<SENDER> parserRegistry;
    private final BindRegistry bindRegistry;
    private final Scheduler scheduler;

    private final BiMap<Class<?>, ArgumentKey, ParserSet<SENDER, ?>> cachedParserSets = new BiHashMap<>();

    ScheduledRequirementResolver(ContextRegistry<SENDER> contextRegistry, ParserRegistry<SENDER> parserRegistry, BindRegistry bindRegistry, Scheduler scheduler) {
        this.contextRegistry = contextRegistry;
        this.parserRegistry = parserRegistry;
        this.bindRegistry = bindRegistry;
        this.scheduler = scheduler;
    }

    @NotNull
    <MATCHER extends ParseableInputMatcher<MATCHER>> List<ScheduledRequirement<?>> prepareRequirements(CommandExecutor<SENDER> executor, Invocation<SENDER> invocation, MATCHER matcher) {
        List<ScheduledRequirement<?>> requirements = new ArrayList<>();

        for (Argument<?> argument : executor.getArguments()) {
            requirements.add(toScheduled(argument, () -> matchArgument(argument, invocation, matcher)));
        }

        for (ContextRequirement<?> contextRequirement : executor.getContextRequirements()) {
            requirements.add(toScheduled(contextRequirement, () -> matchContext(contextRequirement, invocation)));
        }

        for (BindRequirement<?> bindRequirement : executor.getBindRequirements()) {
            requirements.add(toScheduled(bindRequirement, () -> matchBind(bindRequirement)));
        }

        return requirements;
    }

    private ScheduledRequirement<?> toScheduled(Requirement<?> requirement, ThrowingSupplier<RequirementFutureResult<?>, Throwable> resultSupplier) {
        return new ScheduledRequirement<>(requirement, () -> scheduler.supply(requirement.meta().get(Meta.POLL_TYPE), resultSupplier));
    }

    @SuppressWarnings("unchecked")
    private <T, MATCHER extends ParseableInputMatcher<MATCHER>> RequirementFutureResult<T> matchArgument(Argument<T> argument, Invocation<SENDER> invocation, MATCHER matcher) {
        Class<T> rawType = argument.getType().getRawType();
        ParserSet<SENDER, T> parserSet = (ParserSet<SENDER, T>) cachedParserSets.get(rawType, argument.getKey());

        if (parserSet == null) {
            parserSet = parserRegistry.getParserSet(rawType, argument.getKey());
            cachedParserSets.put(rawType, argument.getKey(), parserSet);
        }

        Parser<SENDER, T> parser = parserSet.getValidParserOrThrow(argument);

        return matcher.nextArgument(invocation, argument, parser);
    }

    private <T> RequirementFutureResult<T> matchContext(ContextRequirement<T> contextRequirement, Invocation<SENDER> invocation) {
        return contextRegistry.provideContext(contextRequirement.getType().getRawType(), invocation);
    }

    private <T> RequirementFutureResult<?> matchBind(BindRequirement<T> bindRequirement) {
        Class<T> rawType = bindRequirement.getType().getRawType();
        BindResult<T> instance = bindRegistry.getInstance(rawType);

        if (instance.isOk()) {
            return ParseResult.success(instance.getSuccess());
        }

        return ParseResult.failure(instance.getError());
    }

}
