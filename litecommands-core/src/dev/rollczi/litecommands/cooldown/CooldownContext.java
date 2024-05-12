package dev.rollczi.litecommands.cooldown;

import java.time.Duration;

public class CooldownContext {

    private final String key;
    private final Duration duration;

    public CooldownContext(String key, Duration duration) {
        this.key = key;
        this.duration = duration;
    }

    public String getKey() {
        return key;
    }

    public Duration getDuration() {
        return duration;
    }

}
