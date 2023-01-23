package dev.rollczi.litecommands.modern.extension;

import dev.rollczi.litecommands.modern.LiteCommandsBuilder;
import dev.rollczi.litecommands.modern.LiteCommandsInternalPattern;
import dev.rollczi.litecommands.modern.extension.annotation.LiteAnnotationExtension;

public interface LiteExtension<SENDER> {

    <B extends LiteCommandsBuilder<SENDER, B> & LiteCommandsInternalPattern<SENDER>> void extend(B builder);

    static <SENDER> LiteAnnotationExtension<SENDER> annotation() {
        return new LiteAnnotationExtension<>();
    }

}
