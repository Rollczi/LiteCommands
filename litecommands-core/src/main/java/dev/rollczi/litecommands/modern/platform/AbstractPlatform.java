package dev.rollczi.litecommands.modern.platform;

import dev.rollczi.litecommands.modern.LiteConfiguration;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractPlatform<SENDER, C extends LiteConfiguration> implements Platform<SENDER, C> {

    protected @NotNull C liteConfiguration;

    protected AbstractPlatform(@NotNull C liteConfiguration) {
        this.liteConfiguration = liteConfiguration;
    }

    @Override
    public void setConfiguration(@NotNull C liteConfiguration) {
        this.liteConfiguration = liteConfiguration;
    }

    @Override
    @NotNull
    public C getConfiguration() {
        return liteConfiguration;
    }

}
