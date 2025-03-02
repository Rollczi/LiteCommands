package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.platform.PlatformSender;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import org.jetbrains.annotations.Nullable;

public class PermissionDefaultResolver implements PermissionResolver {

    private final BiPredicate<PlatformSender, String> hasPermission;

    public PermissionDefaultResolver(BiPredicate<PlatformSender, String> hasPermission) {
        this.hasPermission = hasPermission;
    }

    public <SENDER> PermissionDefaultResolver(Class<SENDER> type, BiPredicate<SENDER, String> hasPermission) {
        this.hasPermission = (sender, permission) -> {
            Object handle = sender.getHandle();
            if (!type.isAssignableFrom(handle.getClass())) {
                return false;
            }

            return hasPermission.test(type.cast(handle), permission);
        };
    }

    @Override
    public List<PermissionValidationResult.Verdict> resolve(PlatformSender sender, MetaHolder metaHolder) {
        @Nullable MetaHolder current = metaHolder;

        List<PermissionValidationResult.Verdict> results = new ArrayList<>();
        while (current != null) {
            if (!current.meta().has(Meta.PERMISSIONS)) {
                current = current.parentMeta();
                continue;
            }

            results.add(checkCurrent(sender, current));
            current = current.parentMeta();
        }

        return results;
    }

    private PermissionValidationResult.Verdict checkCurrent(PlatformSender sender, MetaHolder current) {
        List<PermissionValidationResult.Check> checks = new ArrayList<>();
        for (PermissionSet permissionSet : current.meta().get(Meta.PERMISSIONS)) {
            List<String> checked = new ArrayList<>();
            List<String> missing = new ArrayList<>();

            for (String permission : permissionSet.getPermissions()) {
                checked.add(permission);

                if (!hasPermission.test(sender, permission)) {
                    missing.add(permission);
                }
            }

            if (missing.isEmpty()) {
                return PermissionValidationResult.Verdict.permitted(current);
            }

            checks.add(new PermissionValidationResult.Check(checked, missing));
        }

        return new PermissionValidationResult.Verdict(current, checks);
    }

}
