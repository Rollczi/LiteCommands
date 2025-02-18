package dev.rollczi.litecommands.cooldown.event;

import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.invocation.Invocation;
import java.time.Duration;

public class CooldownCommandEvent implements CooldownEvent {

    private final Invocation<?> invocation;
    private String key;
    private Duration duration;
    private boolean cancelled;

    public CooldownCommandEvent(Invocation<?> invocation, String key, Duration duration) {
        this.invocation = invocation;
        this.key = key;
        this.duration = duration;
    }

    public Invocation<?> getInvocation() {
        return invocation;
    }

    @Override
    public Identifier getSender() {
        return invocation.platformSender().getIdentifier();
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
        return Cause.COMMAND;
    }

}
