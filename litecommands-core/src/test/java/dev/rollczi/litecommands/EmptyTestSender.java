package dev.rollczi.litecommands;

import dev.rollczi.litecommands.LiteSender;

public class EmptyTestSender implements LiteSender {

    @Override
    public boolean hasPermission(String permission) { return true; }

    @Override
    public void sendMessage(String message) { }

    @Override
    public Object getSender() { return null; }

}
