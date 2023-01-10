package dev.rollczi.litecommands.modern;

public interface LiteCommandsExtension<SENDER, NEW_BUILDER extends LiteCommandsBuilder<SENDER, ?>> {

    NEW_BUILDER extend(LiteCommandsInternalBuilderPattern<SENDER> builder);

}
