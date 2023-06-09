package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.meta.CommandMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public final class CommandRouteUtils {

    private CommandRouteUtils() {
        throw new UnsupportedOperationException();
    }

    public static <SENDER> void consumeFromRootToChild(CommandRoute<SENDER> child, Consumer<CommandRoute<SENDER>> consumer) {
        if (child.isRoot()) {
            return;
        }

        CommandRoute<SENDER> parent = child.getParent();

        if (parent != null) {
            consumeFromRootToChild(parent, consumer);
        }

        consumer.accept(child);
    }

    public static <SENDER> void consumeFromChildToRoot(CommandRoute<SENDER> child, Consumer<CommandRoute<SENDER>> consumer) {
        if (child.isRoot()) {
            return;
        }

        CommandRoute<SENDER> parent = child.getParent();

        consumer.accept(child);

        if (parent != null) {
            consumeFromChildToRoot(parent, consumer);
        }
    }

    public static <SENDER, T> List<T> collectFromRootToExecutor(CommandRoute<SENDER> route, CommandExecutor<SENDER> executor, Function<CommandMeta, T> collector) {
        List<T> collected = new ArrayList<>();

        consumeFromRootToChild(route, commandRoute -> collected.add(collector.apply(commandRoute.meta())));
        collected.add(collector.apply(executor.getMeta()));

        return Collections.unmodifiableList(collected);
    }
}
