package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.minestom.settings.PermissionResolver;
import dev.rollczi.litecommands.platform.PlatformSettings;

public class LiteMinestomSettings implements PlatformSettings {

    private PermissionResolver permissionResolver;

    public LiteMinestomSettings permissionResolver(PermissionResolver permissionResolver) {
        this.permissionResolver = permissionResolver;
        return this;
    }

    public PermissionResolver getPermissionResolver() {
        return permissionResolver;
    }
}
