package dev.rollczi.litecommands.cooldown;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.cooldown.event.CooldownCommandEvent;
import dev.rollczi.litecommands.cooldown.event.CooldownEvent;
import dev.rollczi.litecommands.event.EventPublisher;
import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.platform.PlatformSender;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.scheduler.SchedulerPoll;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class CooldownService {

    private final Scheduler scheduler;
    private final EventPublisher publisher;
    private final Map<CooldownCompositeKey, CooldownState> cooldowns = new HashMap<>();

    public CooldownService(Scheduler scheduler, EventPublisher publisher) {
        this.scheduler = scheduler;
        this.publisher = publisher;
    }



    @Deprecated
    Optional<CooldownState> getState(CommandExecutor<?> executor, PlatformSender sender) {
        return this.getOperativeContext(executor, sender)
            .map(context -> new CooldownCommandEvent())

        if (cooldownContext == null) {
            return Optional.empty();
        }

        return getState(cooldownContext.getKey(), sender.getIdentifier());
    }

    @Deprecated
    public Optional<CooldownState> getState(String key, Identifier senderIdentifier) {
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

    public boolean markCooldown(CooldownContext context, Identifier senderIdentifier) {
        Instant now = Instant.now();
        Instant expirationTime = now.plus(context.getDuration());

        return true;
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
        scheduler.supplyLater(SchedulerPoll.MAIN, duration, () -> cooldowns.remove(compositeKey));
        return Optional.of(state);
    }



    public boolean clearState(String key, Identifier senderIdentifier) {
        CooldownCompositeKey compositeKey = new CooldownCompositeKey(senderIdentifier, key);

        return cooldowns.remove(compositeKey) != null;
    }

    private Optional<CooldownContext> getOperativeContext(CommandExecutor<?> executor, PlatformSender sender) {
        CooldownContext cooldownContext = executor.metaCollector().findFirst(Meta.COOLDOWN, null);
        if (cooldownContext == null) {
            return Optional.empty();
        }

        String bypassPermission = cooldownContext.getBypassPermission();
        if (!bypassPermission.isEmpty() && sender.hasPermission(bypassPermission)) {
            return Optional.empty();
        }

        return Optional.of(cooldownContext);
    }

}
