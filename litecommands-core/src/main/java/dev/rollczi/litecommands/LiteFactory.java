package dev.rollczi.litecommands;

import dev.rollczi.litecommands.bind.basic.LiteInvocationBind;
import dev.rollczi.litecommands.bind.basic.ArgumentsBind;
import dev.rollczi.litecommands.bind.basic.LiteSenderBind;
import dev.rollczi.litecommands.platform.LiteSender;

public class LiteFactory {

    public static LiteCommandsBuilder builder() {
        return LiteCommandsBuilder.builder()
                .bind(String[].class, new ArgumentsBind())
                .bind(LiteSender.class, new LiteSenderBind())
                .bind(LiteInvocation.class, new LiteInvocationBind());
    }

}
