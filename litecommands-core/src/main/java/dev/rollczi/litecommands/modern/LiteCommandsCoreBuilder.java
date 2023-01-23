package dev.rollczi.litecommands.modern;

public class LiteCommandsCoreBuilder<SENDER> extends LiteCommandsBaseBuilder<SENDER, LiteCommandsCoreBuilder<SENDER>> {

    public LiteCommandsCoreBuilder(Class<SENDER> senderClass) {
        super(senderClass);
    }

}
