package dev.rollczi.litecommands.requirement;

import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.parser.input.ParseableInputMatcher;
import dev.rollczi.litecommands.bind.BindRegistry;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.command.executor.CommandExecutorMatchResult;
import dev.rollczi.litecommands.context.ContextRegistry;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.shared.FailedReason;
import dev.rollczi.litecommands.strict.StrictService;
import dev.rollczi.litecommands.validator.ValidatorResult;
import dev.rollczi.litecommands.validator.requirement.RequirementValidator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import static java.util.concurrent.CompletableFuture.completedFuture;

public class RequirementMatchService<SENDER> {

    private final ScheduledRequirementResolver<SENDER> scheduledRequirementResolver;
    private final StrictService strictService;

    public RequirementMatchService(
        StrictService strictService,
        ContextRegistry<SENDER> contextRegistry,
        ParserRegistry<SENDER> parserRegistry,
        BindRegistry bindRegistry,
        Scheduler scheduler
    ) {
        this.strictService = strictService;
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
                return CompletableFuture.completedFuture(CommandExecutorMatchResult.failed(requirementResult.getFailedReason()));
            }

            if (requirementResult.isSuccessfulNull()) {
                matches.add(toMatch(requirement, null));
                return match(invocation, executor, matches, requirementIterator, matcher);
            }

            Object success = requirementResult.getSuccess();

            // TODO remove START
            List<RequirementValidator<?, ?>> validators = requirement.meta().get(Meta.REQUIREMENT_VALIDATORS);

            for (RequirementValidator<?, ?> validator : validators) {
                ValidatorResult validatorResult = validateRequirement(invocation, executor, requirement, success, validator);

                if (validatorResult.isInvalid()) {
                    return completedFuture(CommandExecutorMatchResult.failed(validatorResult.getInvalidResult()));
                }
            }
            // TODO remove END Add event

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
    @Deprecated
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

    private <R extends Requirement<? extends T>, T> RequirementMatch toMatch(R requirement, T result) {
        return new RequirementMatch(requirement, result);
    }

}
