package dev.rollczi.litecommands;

public final class LiteTestFactory {

    private LiteTestFactory() {}

    public static LiteCommandsBuilder<Void, LiteTestPlatform> builder() {
        return LiteFactory.<Void, LiteTestPlatform>builder()
                .platform(new LiteTestPlatform());
    }

}
