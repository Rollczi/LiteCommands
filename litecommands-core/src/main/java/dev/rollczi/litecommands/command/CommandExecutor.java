package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.argument.PreparedArgument;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.CommandMeta;

import java.util.List;

public interface CommandExecutor<SENDER> {

    List<PreparedArgument<SENDER, ?>> getArguments();

    CommandMeta getMeta();

    CommandExecutorMatchResult match(Invocation<SENDER> invocation, Context context);

    class Context {

        private final int routeBeforeArguments;

        public Context(int routeBeforeArguments) {
            this.routeBeforeArguments = routeBeforeArguments;
        }

        public int getRouteBeforeArguments() {
            return routeBeforeArguments;
        }

    }

}
