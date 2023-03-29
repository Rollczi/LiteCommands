package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.platform.LiteSettings;

public class LiteMinestomSettings implements LiteSettings {

    private boolean nativePermission = false;

    public LiteMinestomSettings nativePermission(boolean nativePermission) {
        this.nativePermission = nativePermission;
        return this;
    }

    boolean isNativePermission() {
        return nativePermission;
    }

}
