package dev.rollczi.litecommands.platform;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractSimplePlatform<SENDER, C extends PlatformSettings> extends AbstractPlatform<SENDER, C> implements Platform<SENDER, C> {

    protected final PlatformSenderFactory<SENDER> senderFactory;

    protected AbstractSimplePlatform(@NotNull C settings, PlatformSenderFactory<SENDER> senderFactory) {
        super(settings);
        this.senderFactory = senderFactory;
    }

    @Override
    public PlatformSender createSender(SENDER nativeSender) {
        return senderFactory.create(nativeSender);
    }

    @Override
    public PlatformSenderFactory<SENDER> getSenderFactory() {
        return senderFactory;
    }

}
