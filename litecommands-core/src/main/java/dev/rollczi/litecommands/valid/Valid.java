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

    @Contract("true, _, _, _ -> fail")
    public static void whenWithContext(boolean bool, ValidationInfo info, LiteComponent.MetaData data, LiteComponent current) {
        if (!bool) {
            return;
        }

        throw new ValidationCommandException(info, ScopeUtils.getLastMessage(info, data, current));
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
