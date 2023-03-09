package dev.rollczi.litecommands.modern;

public interface LiteCommandsExtension<SENDER> {

    void extend(LiteCommandsBuilder<SENDER, ?> builder, LiteCommandsInternalPattern<SENDER> pattern);

}
