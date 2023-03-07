package dev.rollczi.litecommands.test;

import dev.rollczi.litecommands.platform.LiteSender;

public class TestSender implements LiteSender {

    private final TestHandle testHandle;
    private final boolean isOp;

    public TestSender(TestHandle testHandle, boolean isOp) {
        this.testHandle = testHandle;
        this.isOp = isOp;
    }

    @Override
    public boolean hasPermission(String permission) {
        return isOp;
    }

    @Override
    public void sendMessage(String message) {
        testHandle.message(message);
    }

    @Override
    public Object getHandle() {
        return testHandle;
    }
}
