package dev.rollczi.litecommands.cooldown;

import java.time.Duration;
import java.time.Instant;

public class CooldownState {

    private final CooldownContext cooldownContext;
    private final Duration remainingDuration;
    private final Instant expirationTime;

    public CooldownState(CooldownContext cooldownContext, Duration remainingDuration, Instant expirationTime) {
        this.cooldownContext = cooldownContext;
        this.remainingDuration = remainingDuration;
        this.expirationTime = expirationTime;
    }

    public CooldownContext getCooldownContext() {
        return cooldownContext;
    }

    public Duration getRemainingDuration() {
        return remainingDuration;
    }

    public Instant getExpirationTime() {
        return expirationTime;
    }

}
