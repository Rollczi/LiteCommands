package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.modern.LiteConfiguration;

public class LiteBungeeSettings implements LiteConfiguration {

    private boolean nativePermissions = false;

    public LiteBungeeSettings nativePermissions(boolean nativePermissions) {
        this.nativePermissions = nativePermissions;
        return this;
    }

    boolean isNativePermissions() {
        return this.nativePermissions;
    }

}
