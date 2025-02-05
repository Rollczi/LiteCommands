package dev.rollczi.litecommands.cooldown;

import java.time.Duration;
import java.time.Instant;

public class CooldownState {

    private final String key;
    private final Duration duration;
    private final Instant expirationTime;

    public CooldownState(String key, Duration duration) {
        this.key = key;
        this.duration = duration;
        this.expirationTime = Instant.now().plus(duration);
    }

    public String getKey() {
        return key;
    }

    public Duration getDuration() {
        return duration;
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

}
