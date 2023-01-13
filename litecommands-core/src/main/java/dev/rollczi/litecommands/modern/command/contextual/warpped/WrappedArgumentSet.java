package dev.rollczi.litecommands.modern.command.contextual.warpped;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class WrappedArgumentSet {

    private final List<Supplier<WrappedArgumentWrapper<?>>> wrappers = new ArrayList<>();

    public void add(WrappedArgumentWrapper<?> wrapper) {
        wrappers.add(() -> wrapper);
    }

    public void add(Supplier<WrappedArgumentWrapper<?>> wrapper) {
        wrappers.add(wrapper);
    }

    public List<Object> unwrap() {
        List<Object> unwrapped = new ArrayList<>();

        for (Supplier<WrappedArgumentWrapper<?>> wrapper : wrappers) {
            unwrapped.add(wrapper.get().unwrap());
        }

        return unwrapped;
    }

}
