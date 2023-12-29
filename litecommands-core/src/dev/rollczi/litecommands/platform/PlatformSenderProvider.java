package dev.rollczi.litecommands.platform;

@FunctionalInterface
public interface PlatformSenderProvider<SENDER> {

    PlatformSender get(SENDER sender);

}
