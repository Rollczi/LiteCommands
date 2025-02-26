package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.platform.PlatformSender;

public class PermissionServiceImpl implements PermissionService {

    private PermissionResolver permissionResolver = PermissionResolver.createDefault((sender, permission) -> false);

    public PermissionServiceImpl() {
    }

    public PermissionServiceImpl(PermissionResolver permissionResolver) {
        this.permissionResolver = permissionResolver;
    }

    public void setPermissionResolver(PermissionResolver permissionResolver) {
        this.permissionResolver = permissionResolver;
    }

    @Override
    public MissingPermissions validate(PlatformSender sender, MetaHolder metaHolder) {
        return new MissingPermissions(permissionResolver.resolve(sender, metaHolder));
    }

    @Override
    public MissingPermissions validate(PlatformSender sender, Meta meta) {
        return new MissingPermissions(permissionResolver.resolve(sender, MetaHolder.of(meta)));
    }

}
