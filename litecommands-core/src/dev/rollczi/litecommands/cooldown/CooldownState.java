package dev.rollczi.litecommands.cooldown;

import java.time.Duration;
import java.time.Instant;

public class CooldownState {

    private final CooldownContext cooldownContext;
    private final Instant expirationTime;

    public CooldownState(CooldownContext cooldownContext, Instant expirationTime) {
        this.cooldownContext = cooldownContext;
        this.expirationTime = expirationTime;
    }

    public CooldownContext getCooldownContext() {
        return cooldownContext;
    }

    public Duration getRemainingDuration() {
        return Duration.between(Instant.now(), expirationTime);
    }

    public Instant getExpirationTime() {
        return expirationTime;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expirationTime);
    }

    @Override
    public String toString() {
        return "CooldownState{" +
            "cooldownContext=" + cooldownContext.getKey() +
            ", expirationTime=" + expirationTime +
            '}';
    }
}
