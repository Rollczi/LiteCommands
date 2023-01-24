package dev.rollczi.litecommands.modern;

import dev.rollczi.litecommands.modern.annotation.LiteCommandsAnnotationBuilder;
import dev.rollczi.litecommands.modern.annotation.LiteCommandsAnnotationBuilderImpl;
import dev.rollczi.litecommands.modern.core.argument.StringArgument;
import dev.rollczi.litecommands.modern.core.LiteCommandsCoreBuilder;

public final class LiteCommandsFactory {

    private LiteCommandsFactory() {
    }

    public static <SENDER, B extends LiteCommandsCoreBuilder<SENDER, B>> LiteCommandsCoreBuilder<SENDER, B> simple(Class<SENDER> senderClass) {
        return new LiteCommandsCoreBuilder<SENDER, B>(senderClass)
            .argument(String.class, new StringArgument<>())

    }

    @SuppressWarnings("unchecked")
    public static <SENDER, B extends LiteCommandsAnnotationBuilder<SENDER, B>> LiteCommandsAnnotationBuilder<SENDER, B> annotation(Class<SENDER> senderClass) {
        return (B) new LiteCommandsAnnotationBuilderImpl<>(senderClass);
    }

}
