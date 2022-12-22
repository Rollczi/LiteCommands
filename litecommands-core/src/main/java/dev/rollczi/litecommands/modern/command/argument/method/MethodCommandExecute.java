package dev.rollczi.litecommands.modern.command.argument.method;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.CommandExecute;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

class MethodCommandExecute implements CommandExecute {

    private final List<ArgumentContext<?, ?>> contexts = new ArrayList<>();

    MethodCommandExecute(Collection<ArgumentContext<Annotation, Object>> contextBoxes) {
        contexts.addAll(contextBoxes);
    }

    @NotNull
    @Override
    public Iterator<ArgumentContext<?, ?>> iterator() {
        return contexts.iterator();
    }

}
