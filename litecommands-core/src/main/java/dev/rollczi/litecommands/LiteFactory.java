package dev.rollczi.litecommands;

import dev.rollczi.litecommands.inject.basic.LiteInvocationBind;
import dev.rollczi.litecommands.inject.basic.ArgumentsBind;
import dev.rollczi.litecommands.inject.basic.LiteSenderBind;

public class LiteFactory {

    public static LiteCommandsBuilder builder() {
        return LiteCommandsBuilder.builder()
                .bind(String[].class, new ArgumentsBind())
                .bind(LiteSender.class, new LiteSenderBind())
                .bind(LiteInvocation.class, new LiteInvocationBind());
    }

}
