package dev.rollczi.litecommands.validator;

import dev.rollczi.litecommands.command.executor.CommandExecutorMatchResult;
import dev.rollczi.litecommands.command.executor.event.CommandExecutorFoundEvent;
import dev.rollczi.litecommands.command.executor.event.CommandExecutorNotFoundEvent;
import dev.rollczi.litecommands.event.EventListener;
import dev.rollczi.litecommands.event.Subscriber;
import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invocation.Invocation;

@Deprecated
public class ValidatorExecutorController<S> implements EventListener {

    private final ValidatorService<S> validatorService;

    public ValidatorExecutorController(ValidatorService<S> validatorService) {
        this.validatorService = validatorService;
    }

    @Subscriber
    public void onEvent(CommandExecutorFoundEvent<S> event) {
        Flow flow = this.validatorService.validate(event.getInvocation(), event.getExecutor());

        if (flow.isTerminate()) {
            event.cancel(flow.failedReason());
        }

        if (flow.isStopCurrent()) {
            event.setResult(CommandExecutorMatchResult.failed(flow.failedReason()));
        }
    }

    @Subscriber
    void onEvent(CommandExecutorNotFoundEvent event) {
        Flow flow = this.validatorService.validate((Invocation<S>) event.getInvocation(), event.getCommandRoute());
        if (flow.isContinue()) {
            return;
        }

        event.setFailedReason(flow.failedReason());
    }

}
