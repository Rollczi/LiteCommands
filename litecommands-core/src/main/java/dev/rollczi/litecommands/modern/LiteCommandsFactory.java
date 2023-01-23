package dev.rollczi.litecommands.modern;

public final class LiteCommandsFactory {

    private LiteCommandsFactory() {
    }

    public static <SENDER> LiteCommandsCoreBuilder<SENDER> builder(Class<SENDER> senderClass) {
        return new LiteCommandsCoreBuilder<>(senderClass);
    }

}
