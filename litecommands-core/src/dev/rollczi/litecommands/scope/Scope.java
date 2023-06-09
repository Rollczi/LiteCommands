package dev.rollczi.litecommands.scope;

import dev.rollczi.litecommands.meta.CommandMeta;

@FunctionalInterface
public interface Scope {

    boolean isApplicable(Scopeable scopeable);

    static Scope global() {
        return scopeable -> true;
    }

    static Scope command(String name) {
        return scopeable -> scopeable.names().contains(name);
    }

    static Scope command(Class<?> type) {
        return scopeable -> {
            CommandMeta meta = scopeable.meta();

            if (!meta.has(CommandMeta.COMMAND_ORIGIN_TYPE)) {
                return false;
            }

            return meta.get(CommandMeta.COMMAND_ORIGIN_TYPE).equals(type);
        };
    }

}
