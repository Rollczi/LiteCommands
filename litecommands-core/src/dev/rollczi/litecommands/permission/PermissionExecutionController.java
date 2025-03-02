package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.command.executor.CommandExecutorMatchResult;
import dev.rollczi.litecommands.command.executor.event.CommandExecutorFoundEvent;
import dev.rollczi.litecommands.command.executor.event.CommandExecutorNotFoundEvent;
import dev.rollczi.litecommands.event.EventListener;
import dev.rollczi.litecommands.event.Subscriber;
import dev.rollczi.litecommands.priority.PriorityLevel;
import dev.rollczi.litecommands.shared.FailedReason;

public class PermissionExecutionController implements EventListener {

    private final PermissionService permissionService;

    public PermissionExecutionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Subscriber
    void onEvent(CommandExecutorFoundEvent<?> event) {
        MissingPermissions result = this.permissionService.validate(event.getInvocation().platformSender(), event.getExecutor());
        if (result.isPermitted()) {
            return;
        }

        boolean successful = event.getResult().isSuccessful();
        PriorityLevel priority = successful ? PriorityLevel.HIGH : PriorityLevel.NORMAL;
        event.setResult(CommandExecutorMatchResult.failed(FailedReason.of(result, priority)));
    }

    @Subscriber
    void onEvent(CommandExecutorNotFoundEvent event) {
        MissingPermissions result = this.permissionService.validate(event.getInvocation().platformSender(), event.getCommandRoute());
        if (result.isPermitted()) {
            return;
        }

        event.setFailedReason(FailedReason.of(result));
    }

}
