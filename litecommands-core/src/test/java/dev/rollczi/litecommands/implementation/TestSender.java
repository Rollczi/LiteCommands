package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.platform.LiteSender;

class TestSender implements LiteSender {

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

    @Override
    public Object getHandle() {
        return null;
    }

}
