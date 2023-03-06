package dev.rollczi.litecommands.modern.platform;

public interface PlatformSender {

    String getName();

    boolean hasPermission(String permission);

}
