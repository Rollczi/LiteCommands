package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.platform.PlatformSender;

public interface PermissionValidationService {

    PermissionValidationResult validate(MetaHolder metaHolder, PlatformSender sender);

}
