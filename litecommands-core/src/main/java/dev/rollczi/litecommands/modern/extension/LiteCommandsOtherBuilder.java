package dev.rollczi.litecommands.modern.extension;

import dev.rollczi.litecommands.modern.LiteCommands;
import dev.rollczi.litecommands.modern.LiteCommandsBaseBuilder;
import dev.rollczi.litecommands.modern.LiteCommandsInternalBuilderPattern;
import dev.rollczi.litecommands.modern.command.api.StringArgument;

public class LiteCommandsOtherBuilder<SENDER>
    extends LiteCommandsBaseBuilder<SENDER, LiteCommandsOtherBuilder<SENDER>> {

    protected LiteCommandsOtherBuilder(LiteCommandsInternalBuilderPattern<SENDER> pattern) {
        super(pattern);
    }

    @Override
    public LiteCommands<SENDER> register() {
        this.argument(String.class, new StringArgument<>());

        return super.register();
    }

    public LiteCommandsOtherBuilder<SENDER> otherAction() {
        return this;
    }

}
