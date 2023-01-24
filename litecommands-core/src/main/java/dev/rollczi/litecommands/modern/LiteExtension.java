package dev.rollczi.litecommands.modern;

import dev.rollczi.litecommands.modern.annotation.LiteCommandsAnnotationBuilderImpl;

public interface LiteExtension<SENDER> {

    <B extends LiteCommandsBuilder<SENDER, B> & LiteCommandsInternalPattern<SENDER>> void extend(B builder);

    static <SENDER> LiteCommandsAnnotationBuilderImpl<SENDER> annotation() {
        return new LiteCommandsAnnotationBuilderImpl<>();
    }

}
