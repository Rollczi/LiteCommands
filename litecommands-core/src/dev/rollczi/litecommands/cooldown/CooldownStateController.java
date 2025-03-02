package dev.rollczi.litecommands.cooldown;

import dev.rollczi.litecommands.command.executor.CommandExecuteResult;
import dev.rollczi.litecommands.command.executor.event.CommandPostExecutionEvent;
import dev.rollczi.litecommands.command.executor.event.CommandPreExecutionEvent;
import dev.rollczi.litecommands.event.EventListener;
import dev.rollczi.litecommands.event.Subscriber;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.platform.PlatformSender;
import dev.rollczi.litecommands.shared.FailedReason;
import java.util.Optional;

public class CooldownStateController<SENDER> implements EventListener {

    private final CooldownService cooldownService;

    public CooldownStateController(CooldownService cooldownService) {
        this.cooldownService = cooldownService;
    }

    @Subscriber
    public void onEvent(CommandPreExecutionEvent<SENDER> event) {
        Invocation<SENDER> invocation = event.getInvocation();
        PlatformSender sender = invocation.platformSender();
        Optional<CooldownState> currentState = this.cooldownService.getCooldown(event.getExecutor(), sender);

        if (currentState.isPresent()) {
            event.cancel(FailedReason.of(currentState.get()));
        }
    }

    @Subscriber
    public void onEvent(CommandPostExecutionEvent<SENDER> event) {
        CommandExecuteResult result = event.getResult();

        if (!result.isSuccessful()) {
            return;
        }

        cooldownService.markCooldown(event.getInvocation(), event.getExecutor());
    }

}
