package dev.rollczi.litecommands.platform;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface PlatformReceiver {

    @ApiStatus.Experimental
    Comparable<Void> sendMessage(String message);

}
