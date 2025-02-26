package dev.rollczi.litecommands.luckperms;

import dev.rollczi.litecommands.permission.PermissionResolver;
import dev.rollczi.litecommands.platform.PlatformSender;
import java.util.function.BiPredicate;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.query.QueryOptions;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

public class LuckPermsPermissionFactory {

    public static final UUID CONSOLE = new UUID(0, 0);

    private static final Supplier<UserManager> USER_MANAGER = () -> LuckPermsProvider.get().getUserManager();

    public static <PLAYER, CONSOLE> PermissionResolver create(Class<PLAYER> playerType, Class<CONSOLE> consoleType, Function<PLAYER, @Nullable UUID> uuidProvider) {
        return PermissionResolver.createDefault(new ComplexResolver<>(playerType, consoleType, uuidProvider));
    }

    public static <SENDER> PermissionResolver create(Class<SENDER> senderType, Function<SENDER, @Nullable UUID> uuidProvider) {
        return PermissionResolver.createDefault(senderType, (sender, permission) -> hasPermission(uuidProvider.apply(sender), permission));
    }

    private static boolean hasPermission(UUID sender, String permission) {
        if (sender == null) {
            return false;
        }
        if (CONSOLE.equals(sender)) {
            return true;
        }

        UserManager userManager = USER_MANAGER.get();
        User user = userManager.isLoaded(sender)
            ? userManager.getUser(sender)
            : userManager.loadUser(sender).join();

        if (user == null) {
            return false;
        }

        return user.getCachedData().getPermissionData(QueryOptions.nonContextual()).checkPermission(permission).asBoolean();
    }

    private static class ComplexResolver<PLAYER, CONSOLE> implements BiPredicate<PlatformSender, String> {
        private final Class<PLAYER> playerType;
        private final Class<CONSOLE> consoleType;
        private final Function<PLAYER, UUID> uuidProvider;

        public ComplexResolver(Class<PLAYER> playerType, Class<CONSOLE> consoleType, Function<PLAYER, UUID> uuidProvider) {
            this.playerType = playerType;
            this.consoleType = consoleType;
            this.uuidProvider = uuidProvider;
        }

        @Override
        public boolean test(PlatformSender sender, String permission) {
            Object handle = sender.getHandle();
            if (playerType.isAssignableFrom(handle.getClass())) {
                PLAYER player = playerType.cast(handle);
                UUID uuid = uuidProvider.apply(player);
                return hasPermission(uuid, permission);
            }

            return consoleType.isAssignableFrom(handle.getClass());
        }
    }

}
