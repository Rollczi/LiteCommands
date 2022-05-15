package dev.rollczi.litecommands.command.permission;

import dev.rollczi.litecommands.factory.FactoryAnnotationResolver;
import dev.rollczi.litecommands.factory.CommandState;
import panda.std.Option;

import java.util.Arrays;
import java.util.Collection;

class PermissionAnnotationResolver implements FactoryAnnotationResolver<Permissions> {

    @Override
    public Option<CommandState> resolve(Permissions permissions, CommandState commandState) {
        String[] perms = Arrays.stream(permissions.value())
                .map(Permission::value)
                .flatMap(Arrays::stream)
                .toArray(String[]::new);

        return Option.of(commandState.permission(perms));
    }

    @Override
    public Class<Permissions> getAnnotationClass() {
        return Permissions.class;
    }

}
