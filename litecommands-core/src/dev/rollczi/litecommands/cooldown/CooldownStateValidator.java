package dev.rollczi.litecommands.cooldown;

import dev.rollczi.litecommands.argument.suggester.input.SuggestionInput;
import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.validator.Validator;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.jodah.expiringmap.ExpiringMap;

public class CooldownStateValidator<SENDER> implements Validator<SENDER> {

    private final ExpiringMap<CooldownCompositeKey, Instant> cooldowns;

    public CooldownStateValidator() {
        this.cooldowns = ExpiringMap.builder()
            .variableExpiration()
            .build();
    }

    @Override
    public Flow validate(Invocation<SENDER> invocation, MetaHolder metaHolder) {
        if (invocation.arguments() instanceof SuggestionInput<?>) {
            return Flow.continueFlow();
        }

        List<CooldownContext> cooldownContexts = metaHolder.metaCollector().collect(Meta.COOLDOWN);
        if (cooldownContexts.isEmpty()) {
            return Flow.continueFlow();
        }

        return validateCooldown(invocation, cooldownContexts.get(0));
    }

    private Flow validateCooldown(Invocation<SENDER> invocation, CooldownContext cooldownContext) {
        String bypassPermission = cooldownContext.getBypassPermission();
        if (!bypassPermission.isEmpty() && invocation.platformSender().hasPermission(bypassPermission)) {
            return Flow.continueFlow();
        }

        CooldownCompositeKey compositeKey = new CooldownCompositeKey(invocation.platformSender().getIdentifier(), cooldownContext.getKey());

        Instant now = Instant.now();
        Instant expirationTime = cooldowns.get(compositeKey);
        if (expirationTime != null && expirationTime.isAfter(now)) {
            return Flow.terminateFlow(new CooldownState(cooldownContext, Duration.between(now, expirationTime), expirationTime));
        }

        cooldowns.put(compositeKey, now.plus(cooldownContext.getDuration()), cooldownContext.getDuration().toNanos(), TimeUnit.NANOSECONDS);
        return Flow.continueFlow();
    }

}
