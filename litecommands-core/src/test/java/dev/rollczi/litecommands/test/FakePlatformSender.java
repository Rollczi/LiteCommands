package dev.rollczi.litecommands.test;

import dev.rollczi.litecommands.platform.PlatformSender;

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

}
