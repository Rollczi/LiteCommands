package dev.rollczi.litecommands.modern.extension;

import dev.rollczi.litecommands.modern.LiteCommandsExtension;
import dev.rollczi.litecommands.modern.LiteCommandsInternalBuilderPattern;

public class LiteCommandsOtherExtension<SENDER> implements LiteCommandsExtension<SENDER, LiteCommandsOtherBuilder<SENDER>> {

    @Override
    public LiteCommandsOtherBuilder<SENDER> extend(LiteCommandsInternalBuilderPattern<SENDER> builder) {
        return new LiteCommandsOtherBuilder<>(builder);
    }

}