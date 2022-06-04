package dev.rollczi.litecommands.command.permission;

import dev.rollczi.litecommands.factory.FactoryAnnotationResolver;

public final class PermissionUtils {


    public static final FactoryAnnotationResolver<ExecutedPermission> EXECUTED_PERMISSION_RESOLVER = new ExecutedPermissionAnnotationResolver();

    public static final FactoryAnnotationResolver<ExecutedPermissions> EXECUTED_PERMISSIONS_RESOLVER = new ExecutedPermissionsAnnotationResolver();

}
