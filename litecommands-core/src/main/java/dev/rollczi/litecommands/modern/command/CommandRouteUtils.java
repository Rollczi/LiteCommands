package dev.rollczi.litecommands.modern.command;

import java.util.function.Consumer;

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

}
