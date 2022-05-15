package dev.rollczi.litecommands.command.permission;

import dev.rollczi.litecommands.factory.CommandState;
import dev.rollczi.litecommands.factory.FactoryAnnotationResolver;
import panda.std.Option;

import java.util.Arrays;

class ExecutedPermissionAnnotationResolver implements FactoryAnnotationResolver<ExecutedPermission> {

    @Override
    public Option<CommandState> resolve(ExecutedPermission permissions, CommandState commandState) {
        return Option.of(commandState.executedPermission(permissions.value()));
    }

    @Override
    public Class<ExecutedPermission> getAnnotationClass() {
        return ExecutedPermission.class;
    }

}
