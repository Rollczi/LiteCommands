package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.meta.CommandKey;
import dev.rollczi.litecommands.meta.MetaCollector;

import java.util.ArrayList;
import java.util.List;

class CommandRouteMetaCollector implements MetaCollector {

    private final CommandRoute<?> route;

    CommandRouteMetaCollector(CommandRoute<?> route) {
        this.route = route;
    }

    @Override
    public <T> List<T> collect(CommandKey<T> key) {
        List<T> collected = new ArrayList<>();
        CommandRoute<?> current = this.route;

        while (!current.isRoot()) {
            if (current.meta().has(key)) {
                collected.add(current.meta().get(key));
            }

            current = current.getParent();
        }

        return collected;
    }

}
