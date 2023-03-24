package dev.rollczi.litecommands.platform;

public interface PlatformSender {

    String getName();

    boolean hasPermission(String permission);

}
