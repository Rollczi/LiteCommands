package dev.rollczi.litecommands;

import dev.rollczi.litecommands.platform.LiteSender;

public class TestSender implements LiteSender {

    private final TestHandle testHandle;

    public TestSender(TestHandle testHandle) {
        this.testHandle = testHandle;
    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public Object getHandle() {
        return testHandle;
    }
}
