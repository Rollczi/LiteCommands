package dev.rollczi.litecommands.command.permission;

import dev.rollczi.litecommands.factory.CommandState;
import dev.rollczi.litecommands.factory.FactoryAnnotationResolver;
import panda.std.Option;

import java.util.Arrays;

class PermissionAnnotationResolver implements FactoryAnnotationResolver<Permissions> {

    @Override
    public Option<CommandState> resolve(Permissions permissions, CommandState commandState) {
        String[] perms = Arrays.stream(permissions.value())
                .map(Permission::value)
                .flatMap(Arrays::stream)
                .toArray(String[]::new);

        return Option.of(commandState.meta(meta -> meta.addPermission(perms)));
    }

    @Override
    public Class<Permissions> getAnnotationClass() {
        return Permissions.class;
    }

}
