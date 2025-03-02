package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.LiteCommandsInternal;

public interface PlatformFactory<SENDER, SETTINGS extends PlatformSettings> {

    Platform<SENDER, SETTINGS> createPlatform(LiteCommandsInternal<SENDER, SETTINGS> settings);

}
