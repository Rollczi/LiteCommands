package dev.rollczi.litecommands.cooldown;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.platform.PlatformSender;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.scheduler.SchedulerPoll;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Experimental
public class CooldownService {

    private final Scheduler scheduler;
    private final Map<CooldownCompositeKey, CooldownState> cooldowns = new HashMap<>();

    public CooldownService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    Optional<CooldownState> getState(CommandExecutor<?> executor, PlatformSender sender) {
        CooldownContext cooldownContext = getOperativeContext(executor, sender);

        if (cooldownContext == null) {
            return Optional.empty();
        }

        return getState(cooldownContext.getKey(), sender.getIdentifier());
    }

    public Optional<CooldownState> getState(String key, Identifier senderIdentifier) {
        CooldownCompositeKey compositeKey = new CooldownCompositeKey(senderIdentifier, key);
        CooldownState cooldownState = cooldowns.get(compositeKey);

        if (cooldownState != null && !cooldownState.isExpired()) {
            return Optional.of(cooldownState);
        }

        return Optional.empty();
    }

    boolean updateState(CommandExecutor<?> executor, PlatformSender sender) {
        CooldownContext cooldownContext = getOperativeContext(executor, sender);

        if (cooldownContext == null) {
            return false;
        }

        return updateState(cooldownContext, sender.getIdentifier());
    }

    public boolean updateState(CooldownContext context, Identifier senderIdentifier) {
        CooldownCompositeKey compositeKey = new CooldownCompositeKey(senderIdentifier, context.getKey());

        Instant now = Instant.now();
        Instant expirationTime = now.plus(context.getDuration());
        cooldowns.put(compositeKey, new CooldownState(context, expirationTime));
        scheduler.supplyLater(SchedulerPoll.MAIN, context.getDuration(), () -> cooldowns.remove(compositeKey));
        return true;
    }

    public boolean clearState(String key, Identifier senderIdentifier) {
        CooldownCompositeKey compositeKey = new CooldownCompositeKey(senderIdentifier, key);

        return cooldowns.remove(compositeKey) != null;
    }

    @Nullable
    private CooldownContext getOperativeContext(CommandExecutor<?> executor, PlatformSender sender) {
        CooldownContext cooldownContext = executor.metaCollector().findFirst(Meta.COOLDOWN, null);

        if (cooldownContext == null) {
            return null;
        }

        String bypassPermission = cooldownContext.getBypassPermission();

        if (!bypassPermission.isEmpty() && sender.hasPermission(bypassPermission)) {
            return null;
        }

        return cooldownContext;
    }

}
