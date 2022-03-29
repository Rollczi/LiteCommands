package dev.rollczi.litecommands;

public final class LiteTestFactory {

    private LiteTestFactory() {}

    public static LiteCommandsBuilder<Void, LiteTestPlatform> builder() {
        return LiteFactory.<Void, LiteTestPlatform>builder()
                .parameterBind(LiteTestSender.class, new LiteTestSenderBind())
                .argument(LiteTestSender.class, new LiteTestSenderArgument())
                .platform(new LiteTestPlatform());
    }

}
