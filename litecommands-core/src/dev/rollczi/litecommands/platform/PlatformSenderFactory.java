package dev.rollczi.litecommands.platform;

/**
 * Factory for a {@link PlatformSender}
 * @param <SENDER> - Platform related sender e.g. CommandSender, User, CommandSource
 */
public interface PlatformSenderFactory<SENDER> {

    PlatformSender create(SENDER sender);

}
