package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.platform.PlatformSender;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public interface PermissionResolver {

    List<PermissionValidationResult.Verdict> resolve(PlatformSender sender, MetaHolder metaHolder);

    static <SENDER> PermissionResolver createDefault(Class<SENDER> type, BiPredicate<SENDER, String> hasPermission) {
        return new PermissionDefaultResolver(type, hasPermission);
    }

    static PermissionResolver createDefault(BiPredicate<PlatformSender, String> hasPermission) {
        return new PermissionDefaultResolver(hasPermission);
    }

}
