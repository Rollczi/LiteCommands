package dev.rollczi.litecommands.command.permission;

import dev.rollczi.litecommands.factory.FactoryAnnotationResolver;
import dev.rollczi.litecommands.factory.CommandState;
import panda.std.Option;

import java.util.Arrays;

class ExecutedPermissionAnnotationResolver implements FactoryAnnotationResolver<ExecutedPermissions> {

    @Override
    public Option<CommandState> resolve(ExecutedPermissions permissions, CommandState commandState) {
        String[] perms = Arrays.stream(permissions.value()).map(ExecutedPermission::value).toArray(String[]::new);

        return Option.of(commandState.executedPermission(perms));
    }

    @Override
    public Class<ExecutedPermissions> getAnnotationClass() {
        return ExecutedPermissions.class;
    }

}
