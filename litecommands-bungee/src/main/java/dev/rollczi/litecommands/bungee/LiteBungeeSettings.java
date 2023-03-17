package dev.rollczi.litecommands.bungee;

public class LiteBungeeSettings {

    private boolean nativePermissions = false;

    public LiteBungeeSettings nativePermissions(boolean nativePermissions) {
        this.nativePermissions = nativePermissions;
        return this;
    }

    boolean isNativePermissions() {
        return this.nativePermissions;
    }

}
