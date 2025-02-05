package dev.rollczi.litecommands.cooldown.event;

import dev.rollczi.litecommands.event.Event;
import dev.rollczi.litecommands.identifier.Identifier;
import java.time.Duration;

public interface CooldownEvent extends Event {

    Identifier getSender();

    String getKey();

    void setKey(String key);

    Duration getDuration();

    void setDuration(Duration duration);

    boolean isCancelled();

    void setCancelled(boolean cancelled);

    Cause getCause();

    enum Cause {
        COMMAND,
        API,
        OTHER
    }

}
