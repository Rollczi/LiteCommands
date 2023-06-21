package dev.rollczi.litecommands.command.builder;

import dev.rollczi.litecommands.meta.MetaKey;
import dev.rollczi.litecommands.meta.MetaCollector;

import java.util.ArrayList;
import java.util.List;

public class CommandBuilderMetaCollector implements MetaCollector {

    private final CommandBuilder contextBase;

    public CommandBuilderMetaCollector(CommandBuilder contextBase) {
        this.contextBase = contextBase;
    }

    @Override
    public <T> List<T> collect(MetaKey<T> key) {
        List<T> collected = new ArrayList<>();

        CommandBuilder current = this.contextBase;

        while (current != null) {
            if (current.meta().has(key)) {
                collected.add(current.meta().get(key));
            }

            current = current.parent();
        }

        return collected;
    }

}
