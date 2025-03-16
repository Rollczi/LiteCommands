package dev.rollczi.litecommands.cooldown;

import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.permission.PermissionSet;
import dev.rollczi.litecommands.util.StringUtil;
import java.time.Duration;

public class CooldownContext {

    private final String key;
    private final Duration duration;
    private final String bypassPermission;
    private final Meta meta = Meta.create();

    public CooldownContext(String key, Duration duration, String bypassPermission) {
        this.key = key;
        this.duration = duration;
        this.bypassPermission = bypassPermission;
        this.meta.listEditor(Meta.PERMISSIONS)
            .add(new PermissionSet(bypassPermission))
            .apply();
    }

    public CooldownContext(String key, Duration duration) {
        this(key, duration, StringUtil.EMPTY);
    }

    public String getKey() {
        return key;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getBypassPermission() {
        return bypassPermission;
    }

}
