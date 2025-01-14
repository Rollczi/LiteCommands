package dev.rollczi.litecommands.strict;

import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.permission.PermissionStrictHandler;
import dev.rollczi.litecommands.shared.Preconditions;

public class StrictService {

    private StrictMode defaultMode = StrictMode.ENABLED;
    private PermissionStrictHandler permissionStrictHandler = PermissionStrictHandler.DEFAULT;

    public void setDefaultMode(StrictMode defaultMode) {
        Preconditions.notNull(defaultMode, "defaultMode");
        Preconditions.checkArgument(defaultMode != StrictMode.DEFAULT, "Default mode cannot be DEFAULT");

        this.defaultMode = defaultMode;
    }

    public void setPermissionStrictHandler(PermissionStrictHandler permissionStrictHandler) {
        Preconditions.notNull(permissionStrictHandler, "permissionStrictHandler");

        this.permissionStrictHandler = permissionStrictHandler;
    }

    public StrictMode getDefaultMode() {
        return defaultMode;
    }

    public PermissionStrictHandler getPermissionStrictHandler() {
        return permissionStrictHandler;
    }

    public boolean isStrict(MetaHolder metaHolder) {
        StrictMode first = metaHolder.metaCollector().findFirst(Meta.STRICT_MODE);

        switch (first) {
            case ENABLED: return true;
            case DISABLED: return false;
            case DEFAULT:
            default:
                return defaultMode == StrictMode.ENABLED;
        }
    }

}
