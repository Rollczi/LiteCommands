package dev.rollczi.litecommands;

import dev.rollczi.litecommands.platform.LiteSender;

public class LiteTestSender implements LiteSender {

    @Override
    public boolean hasPermission(String permission) { return true; }

    @Override
    public void sendMessage(String message) {
        throw new UnsupportedOperationException("Can't send message on TestSender! content: " + message);
    }

    @Override
    public Object getSender() {
        throw new UnsupportedOperationException("Can't get origin sender of TestSender!");
    }

}
