package dev.rollczi.litecommands.platform;

public interface PlatformSettingsConfigurator<C> {
    C apply(C settings);
}
