package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.argument.FailedReason;
import dev.rollczi.litecommands.argument.input.ArgumentsInputMatcher;
import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invalid.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.invocation.InvocationResult;
import dev.rollczi.litecommands.result.ResultService;
import dev.rollczi.litecommands.validator.ValidatorService;

public class CommandExecuteService<SENDER> {

    private final ValidatorService<SENDER> validatorService;
    private final ResultService<SENDER> resultResolver;

    public CommandExecuteService(ValidatorService<SENDER> validatorService, ResultService<SENDER> resultResolver) {
        this.validatorService = validatorService;
        this.resultResolver = resultResolver;
    }

    public InvocationResult<SENDER> execute(Invocation<SENDER> invocation, ArgumentsInputMatcher<?> matcher, CommandRoute<SENDER> commandRoute) {
        InvocationResult<SENDER> result = execute0(invocation, matcher, commandRoute);

        resultResolver.resolveInvocation(result);
        return result;
    }

    private <MATCHER extends ArgumentsInputMatcher<MATCHER>> InvocationResult<SENDER> execute0(
        Invocation<SENDER> invocation,
        ArgumentsInputMatcher<MATCHER> matcher,
        CommandRoute<SENDER> commandRoute
    ) {
        FailedReason lastFailedReason = null;

        for (CommandExecutor<SENDER> executor : commandRoute.getExecutors()) {
            // Handle matching arguments
            CommandExecutorMatchResult match = executor.match(invocation, matcher.copy());

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
                return InvocationResult.failed(invocation, flow.failedReason());
            }

            if (flow.isStopCurrent()) {
                lastFailedReason = flow.failedReason();
                continue;
            }

            // Execution
            CommandExecuteResult executeResult = match.executeCommand();

            return InvocationResult.success(invocation, executeResult);
        }

        // Handle failed
        if (lastFailedReason != null && !lastFailedReason.isEmpty()) {
            Object reason = lastFailedReason.getReason();

            return InvocationResult.failed(invocation, FailedReason.of(reason));
        }

        return InvocationResult.failed(invocation, FailedReason.of(InvalidUsage.Cause.UNKNOWN_COMMAND));
    }

}
