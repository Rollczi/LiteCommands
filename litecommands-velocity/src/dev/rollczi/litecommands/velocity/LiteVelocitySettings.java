package dev.rollczi.litecommands.velocity;

import dev.rollczi.litecommands.platform.PlatformSettings;

public class LiteVelocitySettings implements PlatformSettings {

    private boolean nativePermissions = false;

    public LiteVelocitySettings nativePermissions(boolean nativePermissions) {
        this.nativePermissions = nativePermissions;
        return this;
    }

    boolean isNativePermissions() {
        return nativePermissions;
    }

}
