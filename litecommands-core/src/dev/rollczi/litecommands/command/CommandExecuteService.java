package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.argument.FailedReason;
import dev.rollczi.litecommands.argument.input.ArgumentsInputMatcher;
import dev.rollczi.litecommands.command.requirement.CommandRequirement;
import dev.rollczi.litecommands.command.requirement.CommandRequirementResult;
import dev.rollczi.litecommands.command.requirement.RequirementMatch;
import dev.rollczi.litecommands.exception.ExceptionHandleService;
import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invalid.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.reflect.LiteCommandsReflectException;
import dev.rollczi.litecommands.result.ResultService;
import dev.rollczi.litecommands.validator.ValidatorService;

import java.util.ArrayList;
import java.util.List;

public class CommandExecuteService<SENDER> {

    private final ValidatorService<SENDER> validatorService;
    private final ResultService<SENDER> resultResolver;
    private final ExceptionHandleService<SENDER> exceptionHandleService;

    public CommandExecuteService(ValidatorService<SENDER> validatorService, ResultService<SENDER> resultResolver, ExceptionHandleService<SENDER> exceptionHandleService) {
        this.validatorService = validatorService;
        this.resultResolver = resultResolver;
        this.exceptionHandleService = exceptionHandleService;
    }

    public CommandExecuteResult execute(Invocation<SENDER> invocation, ArgumentsInputMatcher<?> matcher, CommandRoute<SENDER> commandRoute) {
        CommandExecuteResult executeResult = execute0(invocation, matcher, commandRoute);

        Throwable throwable = executeResult.getThrowable();
        if (throwable != null) {
            exceptionHandleService.resolve(invocation, throwable);
        }

        Object result = executeResult.getResult();
        if (result != null) {
            resultResolver.resolve(invocation, result);
        }

        Object error = executeResult.getError();
        if (error != null) {
            resultResolver.resolve(invocation, error);
        }

        return executeResult;
    }

    private <MATCHER extends ArgumentsInputMatcher<MATCHER>> CommandExecuteResult execute0(
        Invocation<SENDER> invocation,
        ArgumentsInputMatcher<MATCHER> matcher,
        CommandRoute<SENDER> commandRoute
    ) {
        FailedReason lastFailedReason = null;

        for (CommandExecutor<SENDER, ?> executor : commandRoute.getExecutors()) {
            // Handle matching arguments
            CommandExecutorMatchResult match = this.match(executor, invocation, matcher.copy());

            if (match.isFailed()) {
                FailedReason current = match.getFailedReason();

                if (!current.isEmpty()) {
                    lastFailedReason = current;
                }

                continue;
            }

            // Handle validation
            Flow flow = this.validatorService.validate(invocation, commandRoute, executor);

            if (flow.isTerminate()) {
                return CommandExecuteResult.failed(flow.getReason());
            }

            if (flow.isStopCurrent()) {
                lastFailedReason = flow.failedReason();
                continue;
            }

            // Execution
            try {
                return match.executeCommand();
            }
            catch (LiteCommandsReflectException exception) {
                return CommandExecuteResult.thrown(exception.getCause());
            }
            catch (Throwable throwable) {
                return CommandExecuteResult.thrown(throwable);
            }
        }

        // Handle failed
        if (lastFailedReason != null && !lastFailedReason.isEmpty()) {
            Object reason = lastFailedReason.getReason();

            return CommandExecuteResult.failed(reason);
        }

        return CommandExecuteResult.failed(InvalidUsage.Cause.UNKNOWN_COMMAND);
    }

    private  <REQUIREMENT extends CommandRequirement<SENDER, ?>, MATCHER extends ArgumentsInputMatcher<MATCHER>> CommandExecutorMatchResult match(
        CommandExecutor<SENDER, REQUIREMENT> executor,
        Invocation<SENDER> invocation,
        MATCHER matcher
    ) {
        List<RequirementMatch<REQUIREMENT>> results = new ArrayList<>();

        for (REQUIREMENT requirement : executor.getRequirements()) {
            CommandRequirementResult<?> result = requirement.match(invocation, matcher);

            if (!result.isSuccess()) {
                return CommandExecutorMatchResult.failed(result.getFailedReason());
            }

            results.add(new RequirementMatch<>(requirement, result));
        }

        ArgumentsInputMatcher.EndResult endResult = matcher.endMatch();

        if (!endResult.isSuccessful()) {
            return CommandExecutorMatchResult.failed(endResult.getFailedReason());
        }

        return executor.match(results);
    }

}
