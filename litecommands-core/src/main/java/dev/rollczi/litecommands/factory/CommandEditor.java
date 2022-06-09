package dev.rollczi.litecommands.factory;

import java.util.Collection;
import java.util.Set;

public interface CommandEditor {

    State edit(State state);

    CommandEditor NONE = state -> state;

    interface State {

        State name(String name);

        String getName();

        State editChild(String child, CommandEditor editor);

        default State aliases(Collection<String> aliases) {
            return this.aliases(aliases, false);
        }

        State aliases(Collection<String> aliases, boolean removeOld);

        Set<String> getAliases();

        default State permission(Collection<String> permissions) {
            return this.permission(permissions, false);
        }

        State permission(Collection<String> permissions, boolean removeOld);

        default State permissionExcluded(Collection<String> permissions) {
            return this.permissionExcluded(permissions, false);
        }

        State permissionExcluded(Collection<String> permissions, boolean removeOld);

        State cancel(boolean canceled);

    }

}
