package dev.rollczi.litecommands.command.root;

import dev.rollczi.litecommands.factory.CommandState;
import dev.rollczi.litecommands.factory.FactoryAnnotationResolver;
import dev.rollczi.litecommands.shared.StringUtils;
import panda.std.Option;

class RootRouteAnnotationResolver implements FactoryAnnotationResolver<RootRoute> {

    @Override
    public Option<CommandState> resolve(RootRoute annotation, CommandState commandState) {
        return Option.of(commandState
            .name(StringUtils.EMPTY)
            .cancel(false)
        );
    }

    @Override
    public Class<RootRoute> getAnnotationClass() {
        return RootRoute.class;
    }

}
