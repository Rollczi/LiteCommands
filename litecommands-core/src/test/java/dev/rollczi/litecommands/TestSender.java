package dev.rollczi.litecommands;

import dev.rollczi.litecommands.platform.LiteSender;

class TestSender implements LiteSender {

    private final TestHandle testHandle;

    TestSender(TestHandle testHandle) {
        this.testHandle = testHandle;
    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

    @Override
    public Object getHandle() {
        return testHandle;
    }
}
