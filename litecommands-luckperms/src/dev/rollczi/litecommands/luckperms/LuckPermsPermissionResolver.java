package dev.rollczi.litecommands.luckperms;

import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.permission.PermissionDefaultResolver;
import dev.rollczi.litecommands.platform.PlatformSender;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.query.QueryOptions;

import java.util.UUID;
import java.util.function.Supplier;

public class LuckPermsPermissionResolver extends PermissionDefaultResolver {

    private static final Supplier<UserManager> USER_MANAGER = () -> LuckPermsProvider.get().getUserManager();

    public LuckPermsPermissionResolver() {
        super((sender, permission) -> hasPermission(sender, permission));
    }

    private static boolean hasPermission(PlatformSender platformSender, String permission) {
        Identifier identifier = platformSender.getIdentifier();
        if (identifier.equals(Identifier.CONSOLE)) {
            return true;
        }

        UUID sender = identifier.getIdentifier(UUID.class)
            .orElseThrow(() -> new IllegalArgumentException(platformSender.getHandle().getClass().getSimpleName() + " does not have UUID identifier!"));

        UserManager userManager = USER_MANAGER.get();
        User user = userManager.isLoaded(sender)
            ? userManager.getUser(sender)
            : userManager.loadUser(sender).join(); // TODO: Handle async

        if (user == null) {
            return false;
        }

        return user.getCachedData().getPermissionData(QueryOptions.nonContextual()).checkPermission(permission).asBoolean();
    }

}
