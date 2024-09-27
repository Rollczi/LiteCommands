package dev.rollczi.litecommands.requirement;

import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.parser.input.ParseableInputMatcher;
import dev.rollczi.litecommands.bind.BindRegistry;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.command.executor.CommandExecutorMatchResult;
import dev.rollczi.litecommands.context.ContextRegistry;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.shared.FailedReason;
import dev.rollczi.litecommands.strict.StrictService;
import dev.rollczi.litecommands.validator.ValidatorResult;
import dev.rollczi.litecommands.validator.requirement.RequirementValidator;
import dev.rollczi.litecommands.wrapper.Wrap;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import dev.rollczi.litecommands.wrapper.Wrapper;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import static java.util.concurrent.CompletableFuture.completedFuture;

public class RequirementMatchService<SENDER> {

    private final ScheduledRequirementResolver<SENDER> scheduledRequirementResolver;
    private final StrictService strictService;
    private final WrapperRegistry wrapperRegistry;

    public RequirementMatchService(
        StrictService strictService,
        WrapperRegistry wrapperRegistry,
        ContextRegistry<SENDER> contextRegistry,
        ParserRegistry<SENDER> parserRegistry,
        BindRegistry bindRegistry,
        Scheduler scheduler
    ) {
        this.strictService = strictService;
        this.wrapperRegistry = wrapperRegistry;
        this.scheduledRequirementResolver = new ScheduledRequirementResolver<>(contextRegistry, parserRegistry, bindRegistry, scheduler);
    }

    public <MATCHER extends ParseableInputMatcher<MATCHER>> CompletableFuture<CommandExecutorMatchResult> match(
        CommandExecutor<SENDER> executor,
        Invocation<SENDER> invocation,
        MATCHER matcher
    ) {
        List<ScheduledRequirement<?>> scheduledRequirements = scheduledRequirementResolver.prepareRequirements(executor, invocation, matcher);
        return match(invocation, executor, new ArrayList<>(), scheduledRequirements.listIterator(), matcher);
    }

    private CompletableFuture<CommandExecutorMatchResult> match(
        Invocation<SENDER> invocation,
        CommandExecutor<SENDER> executor,
        List<RequirementMatch> matches,
        Iterator<ScheduledRequirement<?>> requirementIterator,
        ParseableInputMatcher<?> matcher
    ) {
        if (!requirementIterator.hasNext()) {
            ParseableInputMatcher.EndResult endResult = matcher.endMatch(strictService.isStrict(executor));

            if (!endResult.isSuccessful()) {
                return completedFuture(CommandExecutorMatchResult.failed(endResult.getFailedReason()));
            }

            RequirementsResult.Builder<SENDER> resultBuilder = RequirementsResult.builder(invocation);

            for (RequirementMatch success : matches) {
                resultBuilder.add(success.getRequirement().getName(), success);
            }

            return completedFuture(executor.match(resultBuilder.build()));
        }

        ScheduledRequirement<?> scheduledRequirement = requirementIterator.next();

        return scheduledRequirement.runMatch().thenCompose(result -> result.asFuture()).thenCompose(requirementResult -> {
            Requirement<?> requirement = scheduledRequirement.getRequirement();

            if (requirementResult.isFailed()) {
                WrapFormat<?, ?> wrapperFormat = requirement.getWrapperFormat();
                Object failedReason = requirementResult.getFailedReason();
                Wrapper wrapper = wrapperRegistry.getWrappedExpectedFactory(wrapperFormat);

                if (failedReason == InvalidUsage.Cause.MISSING_ARGUMENT && wrapper.canCreateEmpty()) {
                    Wrap<?> wrap = wrapper.createEmpty(wrapperFormat);

                    matches.add(new RequirementMatch(requirement, wrap));
                    return match(invocation, executor, matches, requirementIterator, matcher);
                }

                return CompletableFuture.completedFuture(CommandExecutorMatchResult.failed(failedReason));
            }

            if (requirementResult.isSuccessfulNull()) {
                matches.add(toMatch(requirement, null));
                return match(invocation, executor, matches, requirementIterator, matcher);
            }

            Object success = requirementResult.getSuccess();

            List<RequirementValidator<?, ?>> validators = requirement.meta().get(Meta.REQUIREMENT_VALIDATORS);

            for (RequirementValidator<?, ?> validator : validators) {
                ValidatorResult validatorResult = validateRequirement(invocation, executor, requirement, success, validator);

                if (validatorResult.isInvalid()) {
                    return completedFuture(CommandExecutorMatchResult.failed(validatorResult.getInvalidResult()));
                }
            }

            matches.add(toMatch(requirement, success));
            return match(invocation, executor, matches, requirementIterator, matcher).thenApply(executorMatchResult -> {
                for (RequirementCondition condition : requirementResult.getConditions()) {
                    Optional<FailedReason> failedReason = condition.check(invocation, executorMatchResult);

                    if (failedReason.isPresent()) {
                        return CommandExecutorMatchResult.failed(failedReason.get());
                    }
                }

                return executorMatchResult;
            });
        });
    }


    @SuppressWarnings("unchecked")
    private <T> ValidatorResult validateRequirement(
        Invocation<SENDER> invocation,
        CommandExecutor<SENDER> executor,
        Requirement<?> requirement,
        T value,
        RequirementValidator<?, ?> validator
    ) {
        Requirement<T> castedRequirement = (Requirement<T>) requirement;
        RequirementValidator<SENDER, T> casted = (RequirementValidator<SENDER, T>) validator;

        return casted.validate(invocation, executor, castedRequirement, value);
    }

    @SuppressWarnings("unchecked")
    private <R extends Requirement<? extends T>, T> RequirementMatch toMatch(R requirement, T result) {
        WrapFormat<T, ?> wrapperFormat = (WrapFormat<T, ?>) requirement.getWrapperFormat();
        Wrap<T> wrap = wrapperRegistry.wrap(() -> result, wrapperFormat);

        return new RequirementMatch(requirement, wrap);
    }


}
