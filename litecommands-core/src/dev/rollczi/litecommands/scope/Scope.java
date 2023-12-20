package dev.rollczi.litecommands.scope;

import dev.rollczi.litecommands.meta.Meta;

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
            Meta meta = scopeable.meta();

            if (!meta.has(Meta.COMMAND_ORIGIN_TYPE)) {
                return false;
            }

            return meta.get(Meta.COMMAND_ORIGIN_TYPE).contains(type);
        };
    }

}
