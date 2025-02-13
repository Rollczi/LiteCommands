package dev.rollczi.litecommands.cooldown.event;

import dev.rollczi.litecommands.identifier.Identifier;
import java.time.Duration;

public class CooldownApiEvent implements CooldownEvent {

    private final Identifier identifier;
    private String key;
    private Duration duration;
    private boolean cancelled;

    public CooldownApiEvent(Identifier identifier, String key, Duration duration) {
        this.identifier = identifier;
        this.key = key;
        this.duration = duration;
    }

    @Override
    public Identifier getSender() {
        return identifier;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public Cause getCause() {
        return Cause.API;
    }

}
