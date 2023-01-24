package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.modern.contextual.ExpectedContextual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CommandExecutorKey {

    private final List<ExpectedContextual<?>> keys = new ArrayList<>();

    public CommandExecutorKey(List<ExpectedContextual<?>> keys) {
        this.keys.addAll(keys);
    }

    public List<ExpectedContextual<?>> getKeys() {
        return Collections.unmodifiableList(this.keys);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommandExecutorKey)) {
            return false;
        }
        CommandExecutorKey that = (CommandExecutorKey) o;
        return this.keys.equals(that.keys);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.keys);
    }

}
