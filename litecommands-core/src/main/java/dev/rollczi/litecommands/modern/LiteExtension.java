package dev.rollczi.litecommands.modern;

public interface LiteExtension<SENDER> {

    <B extends LiteCommandsBuilder<SENDER, B> & LiteCommandsInternalPattern<SENDER>> void extend(B builder);

}
