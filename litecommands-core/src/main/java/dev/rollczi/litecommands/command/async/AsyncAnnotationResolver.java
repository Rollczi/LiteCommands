package dev.rollczi.litecommands.command.async;

import dev.rollczi.litecommands.factory.CommandState;
import dev.rollczi.litecommands.factory.FactoryAnnotationResolver;
import dev.rollczi.litecommands.meta.CommandMeta;
import panda.std.Option;

class AsyncAnnotationResolver implements FactoryAnnotationResolver<Async> {

    @Override
    public Option<CommandState> resolve(Async async, CommandState commandState) {
        return Option.of(commandState.meta(meta -> meta.set(CommandMeta.ASYNCHRONOUS, true)));
    }

    @Override
    public Class<Async> getAnnotationClass() {
        return Async.class;
    }

}
