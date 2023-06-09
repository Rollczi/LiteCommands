package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.platform.PlatformSettings;

public class LiteMinestomSettings implements PlatformSettings {

    private boolean nativePermission = false;

    public LiteMinestomSettings nativePermission(boolean nativePermission) {
        this.nativePermission = nativePermission;
        return this;
    }

    boolean isNativePermission() {
        return nativePermission;
    }

}
