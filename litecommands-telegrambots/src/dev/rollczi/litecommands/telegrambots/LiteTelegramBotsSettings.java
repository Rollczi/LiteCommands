package dev.rollczi.litecommands.telegrambots;

import dev.rollczi.litecommands.platform.PlatformSettings;
import org.apache.commons.lang3.NotImplementedException;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.function.BiPredicate;

public class LiteTelegramBotsSettings implements PlatformSettings {

    private BiPredicate<User, String> permissionChecker = (user, permission) -> {
        throw new NotImplementedException("Override permissionChecker in LiteTelegramBotsSettings if you want to use permissions");
    };

    public BiPredicate<User, String> getPermissionChecker() {
        return permissionChecker;
    }

    public void setPermissionChecker(BiPredicate<User, String> permissionChecker) {
        this.permissionChecker = permissionChecker;
    }
}
