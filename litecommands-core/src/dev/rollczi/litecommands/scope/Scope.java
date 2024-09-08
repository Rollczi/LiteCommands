package dev.rollczi.litecommands.scope;

import dev.rollczi.litecommands.meta.Meta;

@FunctionalInterface
public interface Scope {

    Scope GLOBAL_SCOPE = scopeable -> true;

    boolean isApplicable(Scopeable scopeable);

    static Scope global() {
        return GLOBAL_SCOPE;
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
