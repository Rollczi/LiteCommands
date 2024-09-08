package dev.rollczi.litecommands.validator;

import dev.rollczi.litecommands.command.executor.event.CommandPreExecutionEvent;
import dev.rollczi.litecommands.event.EventListener;
import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invocation.Invocation;

public class ValidatorExecutionController<SENDER> implements EventListener<CommandPreExecutionEvent> {

    private final ValidatorService<SENDER> validatorService;

    public ValidatorExecutionController(ValidatorService<SENDER> validatorService) {
        this.validatorService = validatorService;
    }

    @Override
    public void onEvent(CommandPreExecutionEvent event) {
        Flow flow = this.validatorService.validate((Invocation<SENDER>) event.getInvocation(), event.getExecutor());

        if (flow.isTerminate()) {
            event.stopFlow(flow.failedReason());
        }

        if (flow.isStopCurrent()) {
            event.skipFlow(flow.failedReason());
        }
    }

}
