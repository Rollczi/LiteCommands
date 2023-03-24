package dev.rollczi.litecommands.velocity;

import dev.rollczi.litecommands.platform.LiteSettings;

public class LiteVelocitySettings implements LiteSettings {

    private boolean nativePermissions = false;

    public LiteVelocitySettings nativePermissions(boolean nativePermissions) {
        this.nativePermissions = nativePermissions;
        return this;
    }

    boolean isNativePermissions() {
        return nativePermissions;
    }

}
