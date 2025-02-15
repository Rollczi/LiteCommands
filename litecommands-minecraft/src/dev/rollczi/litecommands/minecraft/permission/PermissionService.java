package dev.rollczi.litecommands.minecraft.permission;

import dev.rollczi.litecommands.platform.PlatformSender;
import java.util.concurrent.CompletableFuture;

public interface PermissionService<SENDER> {

    CompletableFuture<Boolean> hasPermission(PlatformSender platformSender, SENDER sender, String permission);

}
