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
                .typeBind(String[].class, new ArgumentsBind())
                .typeBind(LiteSender.class, new LiteSenderBind())
                .typeBind(LiteInvocation.class, new LiteInvocationBind());
    }

}
