package dev.rollczi.litecommands.platform;

public interface LiteSender {

    boolean hasPermission(String permission);

    void sendMessage(String message);

    Object getSender();

}
