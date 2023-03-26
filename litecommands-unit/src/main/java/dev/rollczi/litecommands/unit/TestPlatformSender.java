package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.platform.PlatformSender;

public class TestPlatformSender implements PlatformSender {

    public TestPlatformSender() {
    }

    @Override
    public String getName() {
        return "TestSender";
    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

}
