package dev.rollczi.litecommands;

import dev.rollczi.litecommands.bind.basic.ArgAnnotationBind;
import dev.rollczi.litecommands.bind.basic.JoinerAnnotationBind;
import dev.rollczi.litecommands.bind.basic.LiteInvocationBind;
import dev.rollczi.litecommands.bind.basic.ArgumentsBind;
import dev.rollczi.litecommands.bind.basic.LiteSenderBind;
import dev.rollczi.litecommands.platform.LitePlatformManager;
import dev.rollczi.litecommands.platform.LiteSender;

public class LiteFactory {

    public static <SENDER, P extends LitePlatformManager<SENDER>> LiteCommandsBuilder<SENDER, P> builder() {
        return LiteCommandsBuilder.<SENDER, P>builder()
                .bind(new ArgAnnotationBind())
                .bind(new JoinerAnnotationBind())
                .bind(String[].class, new ArgumentsBind())
                .bind(LiteSender.class, new LiteSenderBind())
                .bind(LiteInvocation.class, new LiteInvocationBind());
    }

}
