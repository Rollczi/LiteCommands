package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.platform.PlatformSender;

public interface PermissionService {

    MissingPermissions validate(PlatformSender sender, MetaHolder metaHolder);

    MissingPermissions validate(PlatformSender sender, Meta meta);

    default boolean isPermitted(PlatformSender sender, MetaHolder metaHolder) {
        return validate(sender, metaHolder).isPermitted();
    }

    default boolean isPermitted(PlatformSender sender, Meta meta) {
        return validate(sender, meta).isPermitted();
    }

}
