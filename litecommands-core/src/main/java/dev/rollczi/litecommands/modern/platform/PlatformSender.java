package dev.rollczi.litecommands.modern.platform;

import dev.rollczi.litecommands.modern.LiteCommandsFactory;

public interface PlatformSender {

    String getName();

    boolean hasPermission(String permission);

    void sendMessage(String message);

}
