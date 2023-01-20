package dev.rollczi.litecommands.modern.extension.annotation;

import dev.rollczi.litecommands.modern.LiteCommandsInternalBuilderPattern;
import dev.rollczi.litecommands.modern.extension.LiteCommandsExtension;

public class LiteCommandsAnnotationExtension<SENDER> implements LiteCommandsExtension<SENDER, LiteCommandsAnnotationBuilder<SENDER>> {

    @Override
    public LiteCommandsAnnotationBuilder<SENDER> extend(LiteCommandsInternalBuilderPattern<SENDER> builder) {
        return new LiteCommandsAnnotationBuilder<>(builder);
    }

}