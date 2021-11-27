package dev.rollczi.litecommands.valid;

import dev.rollczi.litecommands.component.LiteComponent;
import dev.rollczi.litecommands.component.ScopeUtils;
import org.jetbrains.annotations.Contract;

public class Valid {

    @Contract("true, _, _ -> fail")
    public static void when(boolean bool, ValidationInfo info, String message) {
        if (!bool) {
            return;
        }

        throw new ValidationCommandException(info, message);
    }

    @Contract("true, _, _ -> fail")
    public static void when(boolean bool, ValidationInfo info, LiteComponent.ContextOfResolving context) {
        if (!bool) {
            return;
        }

        throw new ValidationCommandException(info, ScopeUtils.getLastMessage(info, context));
    }

    @Contract("_, _ -> fail")
    public static void throwWithContext(ValidationInfo info, LiteComponent.ContextOfResolving context) {
        throw new ValidationCommandException(info, ScopeUtils.getLastMessage(info, context));
    }

    @Contract("true, _ -> fail")
    public static void when(boolean bool, ValidationInfo info) {
        if (!bool) {
            return;
        }

        throw new ValidationCommandException(info);
    }

    @Contract("true, _ -> fail")
    public static void when(boolean bool, String customMessage) {
        when(bool, ValidationInfo.CUSTOM, customMessage);
    }

}
