package dev.rollczi.litecommands.validator;

import dev.rollczi.litecommands.command.executor.event.CommandExecutionEvent;
import dev.rollczi.litecommands.event.EventListener;
import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invocation.Invocation;

public class ValidatorExecutionController<SENDER> implements EventListener<CommandExecutionEvent> {

    private final ValidatorService<SENDER> validatorService;

    public ValidatorExecutionController(ValidatorService<SENDER> validatorService) {
        this.validatorService = validatorService;
    }

    @Override
    public void onEvent(CommandExecutionEvent event) {
        Flow flow = this.validatorService.validate((Invocation<SENDER>) event.getInvocation(), event.getExecutor());

        if (flow.isTerminate()) {
            event.terminateFlow(flow.failedReason());
        }

        if (flow.isStopCurrent()) {
            event.skipFlow(flow.failedReason());
        }
    }

}
