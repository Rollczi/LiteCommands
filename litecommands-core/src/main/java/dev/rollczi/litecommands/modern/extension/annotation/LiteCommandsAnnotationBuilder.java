package dev.rollczi.litecommands.modern.extension.annotation;

import dev.rollczi.litecommands.modern.LiteCommands;
import dev.rollczi.litecommands.modern.LiteCommandsBaseBuilder;
import dev.rollczi.litecommands.modern.LiteCommandsInternalBuilderPattern;

public class LiteCommandsAnnotationBuilder<SENDER> extends LiteCommandsBaseBuilder<SENDER, LiteCommandsAnnotationBuilder<SENDER>> {

    protected LiteCommandsAnnotationBuilder(LiteCommandsInternalBuilderPattern<SENDER> pattern) {
        super(pattern);
    }

    @Override
    public LiteCommands<SENDER> register() {
        return super.register();
    }

}
