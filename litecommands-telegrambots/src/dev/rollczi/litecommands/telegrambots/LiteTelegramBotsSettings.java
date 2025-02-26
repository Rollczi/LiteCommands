package dev.rollczi.litecommands.telegrambots;

import dev.rollczi.litecommands.platform.PlatformSettings;
import org.apache.commons.lang3.NotImplementedException;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Set;
import java.util.function.BiPredicate;

public class LiteTelegramBotsSettings implements PlatformSettings {

    private Set<Character> commandPrefixes = Set.of('/');
    private BiPredicate<User, String> permissionChecker = (user, permission) -> {
        throw new NotImplementedException("Override permissionChecker in LiteTelegramBotsSettings if you want to use permissions");
    };

    public LiteTelegramBotsSettings commandPrefixes(Set<Character> commandPrefixes) {
        this.commandPrefixes = commandPrefixes;
        return this;
    }

    public Set<Character> getCommandPrefixes() {
        return commandPrefixes;
    }

    public BiPredicate<User, String> getPermissionChecker() {
        return permissionChecker;
    }

    public void setPermissionChecker(BiPredicate<User, String> permissionChecker) {
        this.permissionChecker = permissionChecker;
    }
}
