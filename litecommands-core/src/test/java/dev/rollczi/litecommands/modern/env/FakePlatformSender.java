package dev.rollczi.litecommands.modern.env;

import dev.rollczi.litecommands.modern.platform.PlatformSender;

public class FakePlatformSender implements PlatformSender {

    private final FakeSender fakeSender;

    public FakePlatformSender(FakeSender fakeSender) {
        this.fakeSender = fakeSender;
    }

    @Override
    public String getName() {
        return "FakeSender";
    }

}
