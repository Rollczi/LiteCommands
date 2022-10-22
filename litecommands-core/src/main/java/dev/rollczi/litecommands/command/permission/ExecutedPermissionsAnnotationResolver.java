package dev.rollczi.litecommands.command.permission;

import dev.rollczi.litecommands.factory.CommandState;
import dev.rollczi.litecommands.factory.FactoryAnnotationResolver;
import panda.std.Option;

import java.util.Arrays;

class ExecutedPermissionsAnnotationResolver implements FactoryAnnotationResolver<ExecutedPermissions> {

    @Override
    public Option<CommandState> resolve(ExecutedPermissions permissions, CommandState commandState) {
        String[] perms = Arrays.stream(permissions.value())
                .map(ExecutedPermission::value)
                .flatMap(Arrays::stream)
                .toArray(String[]::new);

        return Option.of(commandState.meta(meta -> meta.addExcludedPermission(perms)));
    }

    @Override
    public Class<ExecutedPermissions> getAnnotationClass() {
        return ExecutedPermissions.class;
    }

}
