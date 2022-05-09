package dev.rollczi.litecommands.platform;

public interface LiteSender {

    boolean hasPermission(String permission);

    Object getHandle();

}
