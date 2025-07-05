package dev.rollczi.litecommands.cooldown;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.cooldown.event.CooldownApiEvent;
import dev.rollczi.litecommands.cooldown.event.CooldownCommandEvent;
import dev.rollczi.litecommands.cooldown.event.CooldownEvent;
import dev.rollczi.litecommands.event.EventPublisher;
import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.permission.PermissionService;
import dev.rollczi.litecommands.platform.PlatformSender;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.scheduler.SchedulerType;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class CooldownService {

    private final Scheduler scheduler;
    private final PermissionService permissionService;
    private final EventPublisher publisher;
    private final Map<CooldownCompositeKey, CooldownState> cooldowns = new HashMap<>();

    public CooldownService(Scheduler scheduler, PermissionService permissionService, EventPublisher publisher) {
        this.scheduler = scheduler;
        this.permissionService = permissionService;
        this.publisher = publisher;
    }

    public Optional<CooldownState> getCooldown(CommandExecutor<?> executor, PlatformSender sender) {
        return this.getOperativeContext(executor, sender)
            .flatMap(cooldownContext -> this.getCooldown(cooldownContext.getKey(), sender.getIdentifier()));
    }

    public Optional<CooldownState> getCooldown(String key, Identifier senderIdentifier) {
        CooldownCompositeKey compositeKey = new CooldownCompositeKey(senderIdentifier, key);
        CooldownState cooldownState = cooldowns.get(compositeKey);
        if (cooldownState != null && !cooldownState.isExpired()) {
            return Optional.of(cooldownState);
        }

        return Optional.empty();
    }

    public Optional<CooldownState> markCooldown(Invocation<?> invocation, CommandExecutor<?> executor) {
        return this.getOperativeContext(executor, invocation.platformSender())
            .map(context -> new CooldownCommandEvent(invocation, context.getKey(), context.getDuration()))
            .flatMap(event -> this.processChange(event));
    }

    public Optional<CooldownState> markCooldown(CooldownContext context, Identifier senderIdentifier) {
        return this.processChange(new CooldownApiEvent(senderIdentifier, context.getKey(), context.getDuration()));
    }

    private Optional<CooldownState> processChange(CooldownEvent event) {
        CooldownEvent publishedEvent = this.publisher.publish(event);
        if (publishedEvent.isCancelled()) {
            return Optional.empty();
        }

        CooldownCompositeKey compositeKey = new CooldownCompositeKey(publishedEvent.getSender(), publishedEvent.getKey());
        Duration duration = publishedEvent.getDuration();
        CooldownState state = new CooldownState(publishedEvent.getKey(), duration);

        cooldowns.put(compositeKey, state);
        scheduler.supplyLater(SchedulerType.MAIN, duration, () -> cooldowns.remove(compositeKey));
        return Optional.of(state);
    }

    public boolean removeCooldown(String key, Identifier senderIdentifier) {
        CooldownCompositeKey compositeKey = new CooldownCompositeKey(senderIdentifier, key);

        return cooldowns.remove(compositeKey) != null;
    }

    private Optional<CooldownContext> getOperativeContext(CommandExecutor<?> executor, PlatformSender sender) {
        CooldownContext context = executor.metaCollector().findFirst(Meta.COOLDOWN, null);
        if (context == null) {
            return Optional.empty();
        }

        if (!context.getBypassPermission().isEmpty() && permissionService.isPermitted(sender, context.getPermissions())) {
            return Optional.empty();
        }

        return Optional.of(context);
    }

}
