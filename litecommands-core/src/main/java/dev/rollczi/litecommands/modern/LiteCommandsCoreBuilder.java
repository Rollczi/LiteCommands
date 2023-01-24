package dev.rollczi.litecommands.modern;

public class LiteCommandsCoreBuilder<SENDER, B extends LiteCommandsCoreBuilder<SENDER, B>> extends LiteCommandsBaseBuilder<SENDER, B> {

    public LiteCommandsCoreBuilder(Class<SENDER> senderClass) {
        super(senderClass);
    }

}
