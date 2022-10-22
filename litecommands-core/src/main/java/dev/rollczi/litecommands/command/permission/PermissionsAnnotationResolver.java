package dev.rollczi.litecommands.command.permission;

import dev.rollczi.litecommands.factory.CommandState;
import dev.rollczi.litecommands.factory.FactoryAnnotationResolver;
import panda.std.Option;

class PermissionsAnnotationResolver implements FactoryAnnotationResolver<Permission> {

    @Override
    public Option<CommandState> resolve(Permission permissions, CommandState commandState) {
        return Option.of(commandState.meta(meta -> meta.addPermission(permissions.value())));
    }

    @Override
    public Class<Permission> getAnnotationClass() {
        return Permission.class;
    }

}
