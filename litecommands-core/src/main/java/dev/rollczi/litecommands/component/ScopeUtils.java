package dev.rollczi.litecommands.component;

import dev.rollczi.litecommands.valid.ValidationInfo;
import org.jetbrains.annotations.Nullable;
import panda.std.Option;

import java.util.ListIterator;

public final class ScopeUtils {

    private ScopeUtils() {}

    @Nullable
    public static String getLastMessage(ValidationInfo info, LiteComponent.Data data, LiteComponent current) {
        ListIterator<LiteComponent> iterator = data.getTracedResolvers().listIterator();
        Option<String> message = current.getScope().getMessage(info);

        while (iterator.hasPrevious()) {
            if (message.isPresent()) {
                return message.get();
            }

            message = current.getScope().getMessage(info);
        }

        return message.getOrNull();
    }


}
