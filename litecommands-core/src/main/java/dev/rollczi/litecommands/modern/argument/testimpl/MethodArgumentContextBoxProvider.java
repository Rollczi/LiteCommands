package dev.rollczi.litecommands.modern.argument.testimpl;

import dev.rollczi.litecommands.modern.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.argument.ArgumentContextBoxProvider;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MethodArgumentContextBoxProvider implements ArgumentContextBoxProvider<Annotation, Object> {

    private final List<ArgumentContext<Annotation, Object>> boxes = new ArrayList<>();

    @SafeVarargs
    public MethodArgumentContextBoxProvider(ArgumentContext<? extends Annotation, ?>... contextBoxes) {
        for (ArgumentContext<? extends Annotation, ?> box : contextBoxes) {
            if (box == null) {
                throw new IllegalArgumentException("ArgumentContextBox cannot be null");
            }

            boxes.add((ArgumentContext<Annotation, Object>) box);
        }
    }

    @NotNull
    @Override
    public Iterator<ArgumentContext<Annotation, Object>> iterator() {
        return boxes.iterator();
    }

}
