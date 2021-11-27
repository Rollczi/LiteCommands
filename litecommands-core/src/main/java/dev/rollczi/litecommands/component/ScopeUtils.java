package dev.rollczi.litecommands.component;

import dev.rollczi.litecommands.valid.ValidationInfo;
import org.jetbrains.annotations.Nullable;
import panda.std.Option;

import java.util.List;
import java.util.ListIterator;

public final class ScopeUtils {

    private ScopeUtils() {}

    @Nullable
    public static String getLastMessage(ValidationInfo info, LiteComponent.ContextOfResolving context) {
        List<LiteComponent> resolvers = context.getTracesOfResolvers();
        ListIterator<LiteComponent> iterator = resolvers.listIterator(resolvers.size());
        Option<String> message = Option.none();

        while (iterator.hasPrevious() && !message.isPresent()) {
            message = iterator.previous().getScope().getMessage(info);
        }

        return message.getOrNull();
    }


}
