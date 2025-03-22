package dev.rollczi.litecommands.luckperms;

import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.permission.PermissionDefaultResolver;
import dev.rollczi.litecommands.platform.PlatformSender;
import java.util.concurrent.CompletableFuture;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.query.QueryOptions;

import java.util.UUID;
import java.util.function.Supplier;

public class LuckPermsPermissionResolver extends PermissionDefaultResolver {

    private static final Supplier<UserManager> USER_MANAGER = () -> LuckPermsProvider.get().getUserManager();

    private LuckPermsPermissionResolver(boolean lazy) {
        super((sender, permission) -> hasPermission(sender, permission, lazy));
    }

    private static boolean hasPermission(PlatformSender platformSender, String permission, boolean lazy) {
        Identifier identifier = platformSender.getIdentifier();
        if (identifier.equals(Identifier.CONSOLE)) {
            return true;
        }

        UUID sender = identifier.getIdentifier(UUID.class)
            .orElseThrow(() -> new IllegalArgumentException(platformSender.getHandle().getClass().getSimpleName() + " does not have UUID identifier!"));

        UserManager userManager = USER_MANAGER.get();
        User user = userManager.getUser(sender);

        if (user == null) {
            CompletableFuture<User> loading = userManager.loadUser(sender);
            if (lazy) {
                return false;
            }

            user = loading.join();
        }

        return user.getCachedData().getPermissionData(QueryOptions.nonContextual()).checkPermission(permission).asBoolean();
    }

    public static LuckPermsPermissionResolver lazy() {
        return new LuckPermsPermissionResolver(true);
    }

    public static LuckPermsPermissionResolver blocking() {
        return new LuckPermsPermissionResolver(false);
    }

}
