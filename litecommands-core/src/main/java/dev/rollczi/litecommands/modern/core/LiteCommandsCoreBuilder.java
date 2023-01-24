package dev.rollczi.litecommands.modern.core;

import dev.rollczi.litecommands.modern.LiteCommandsBaseBuilder;

public class LiteCommandsCoreBuilder<SENDER, B extends LiteCommandsCoreBuilder<SENDER, B>> extends LiteCommandsBaseBuilder<SENDER, B> {

    public LiteCommandsCoreBuilder(Class<SENDER> senderClass) {
        super(senderClass);
    }

}
