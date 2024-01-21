package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.platform.PlatformSettings;
import org.jetbrains.annotations.ApiStatus;

public class LiteMinestomSettings implements PlatformSettings {

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "3.4.0")
    public LiteMinestomSettings nativePermission(boolean nativePermission) {
        return this;
    }

}
