package dev.rollczi.litecommands.command.executor;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.ParserChainAccessor;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.parser.ParserSet;
import dev.rollczi.litecommands.argument.parser.input.ParseableInputMatcher;
import dev.rollczi.litecommands.bind.BindRegistry;
import dev.rollczi.litecommands.context.ContextRegistry;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.requirement.BindRequirement;
import dev.rollczi.litecommands.requirement.ContextRequirement;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.requirement.RequirementResult;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.shared.BiHashMap;
import dev.rollczi.litecommands.shared.BiMap;
import dev.rollczi.litecommands.shared.ThrowingSupplier;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import panda.std.Result;

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

    private ScheduledRequirement<?> toScheduled(Requirement<?> requirement, ThrowingSupplier<RequirementResult<?>, Throwable> resultSupplier) {
        return new ScheduledRequirement<>(requirement, () -> scheduler.supply(requirement.meta().get(Meta.POLL_TYPE), resultSupplier));
    }

    @SuppressWarnings("unchecked")
    private <PARSED, MATCHER extends ParseableInputMatcher<MATCHER>> RequirementResult<PARSED> matchArgument(Argument<PARSED> argument, Invocation<SENDER> invocation, MATCHER matcher) {
        WrapFormat<PARSED, ?> wrapFormat = argument.getWrapperFormat();
        ParserSet<SENDER, PARSED> parserSet = (ParserSet<SENDER, PARSED>) cachedParserSets.get(wrapFormat.getParsedType(), argument.getKey());

        if (parserSet == null) {
            parserSet = parserRegistry.getParserSet(wrapFormat.getParsedType(), argument.getKey());
            cachedParserSets.put(wrapFormat.getParsedType(), argument.getKey(), parserSet);
        }

        Parser<SENDER, PARSED> parser = parserSet.getValidParser(invocation, argument);

        return matcher.nextArgument(invocation, argument, parser);
    }

    private <PARSED> RequirementResult<PARSED> matchContext(ContextRequirement<PARSED> contextRequirement, Invocation<SENDER> invocation) {
        return contextRegistry.provideContext(contextRequirement.getWrapperFormat().getParsedType(), invocation);
    }

    private <PARSED> RequirementResult<?> matchBind(BindRequirement<PARSED> bindRequirement) {
        WrapFormat<PARSED, ?> wrapFormat = bindRequirement.getWrapperFormat();
        Result<PARSED, String> instance = bindRegistry.getInstance(wrapFormat.getParsedType());

        if (instance.isOk()) {
            return ParseResult.success(instance.get());
        }

        return ParseResult.failure(instance.getError());
    }

}
