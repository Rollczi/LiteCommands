package dev.rollczi.litecommands.modern.extension;

import dev.rollczi.litecommands.modern.LiteCommandsBuilder;
import dev.rollczi.litecommands.modern.LiteCommandsInternalBuilderPattern;

public interface LiteCommandsExtension<SENDER, NEW_BUILDER extends LiteCommandsBuilder<SENDER, ?>> {

    NEW_BUILDER extend(LiteCommandsInternalBuilderPattern<SENDER> builder);

}
