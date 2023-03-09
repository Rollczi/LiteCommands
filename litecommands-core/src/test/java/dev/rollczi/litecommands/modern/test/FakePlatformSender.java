package dev.rollczi.litecommands.modern.test;

import dev.rollczi.litecommands.modern.platform.PlatformSender;

public class FakePlatformSender implements PlatformSender {

    public FakePlatformSender() {
    }

    @Override
    public String getName() {
        return "FakeSender";
    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

    @Override
    public void sendMessage(String message) {
    }

}
