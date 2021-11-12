package dev.rollczi.litecommands;

public interface LiteSender {

    boolean hasPermission(String permission);

    void sendMessage(String message);

    Object getSender();

}
