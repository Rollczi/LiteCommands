package dev.rollczi.litecommands.modern.extension.annotation;

import dev.rollczi.litecommands.modern.LiteCommands;
import dev.rollczi.litecommands.modern.LiteCommandsBaseBuilder;
import dev.rollczi.litecommands.modern.LiteCommandsInternalBuilderPattern;
import dev.rollczi.litecommands.modern.command.api.StringArgument;

public class LiteCommandsAnnotationBuilder<SENDER>
    extends LiteCommandsBaseBuilder<SENDER, LiteCommandsAnnotationBuilder<SENDER>> {

    protected LiteCommandsAnnotationBuilder(LiteCommandsInternalBuilderPattern<SENDER> pattern) {
        super(pattern);
    }

    @Override
    public LiteCommands<SENDER> register() {
        this.argument(String.class, new StringArgument<>());

        return super.register();
    }

    public LiteCommandsAnnotationBuilder<SENDER> otherAction() {
        return this;
    }

}
