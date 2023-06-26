package dev.rollczi.litecommands.schematic;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.invocation.Invocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SchematicInput<SENDER> {

    private final CommandRoute<SENDER> lastRoute;
    private final @Nullable CommandExecutor<SENDER, ?> executor;
    private final Invocation<SENDER> invocation;

    public SchematicInput(CommandRoute<SENDER> lastRoute, @Nullable CommandExecutor<SENDER, ?> executor, Invocation<SENDER> invocation) {
        this.lastRoute = lastRoute;
        this.executor = executor;
        this.invocation = invocation;
    }

    public Invocation<SENDER> getInvocation() {
        return invocation;
    }

    public CommandRoute<SENDER> getLastRoute() {
        return lastRoute;
    }

    public @Nullable CommandExecutor<SENDER, ?> getExecutor() {
        return executor;
    }

    public List<CommandRoute<SENDER>> collectRoutes() {
        List<CommandRoute<SENDER>> routes = new ArrayList<>();

        CommandRoute<SENDER> currentRoute = lastRoute;

        while (currentRoute != null && !currentRoute.isRoot()) {
            routes.add(currentRoute);
            currentRoute = currentRoute.getParent();
        }

        Collections.reverse(routes);
        return routes;
    }

}
