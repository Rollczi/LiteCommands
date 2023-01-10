package dev.rollczi.litecommands.modern.extension.annotation.method;

import dev.rollczi.litecommands.modern.command.CommandExecuteResult;
import dev.rollczi.litecommands.modern.command.Invocation;
import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.CommandExecutor;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpped.WrappedArgumentSet;
import org.jetbrains.annotations.NotNull;
import panda.std.Result;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

class MethodCommandExecutor implements CommandExecutor {

    private final Function<Object[], Object> invoker;
    private final List<ArgumentContext<?, ?>> contexts = new ArrayList<>();

    MethodCommandExecutor(Function<Object[], Object> invoker, List<ArgumentContext<Annotation, Object>> contexts) {
        this.invoker = invoker;
        this.contexts.addAll(contexts);
    }

    @NotNull
    @Override
    public Iterator<ArgumentContext<?, ?>> iterator() {
        return contexts.iterator();
    }

    @Override
    public <SENDER> CommandExecuteResult execute(Invocation<SENDER> invocation, WrappedArgumentSet arguments) {
        List<Supplier<?>> list = new ArrayList<>();
        Iterator<Object> argumentsIterator = arguments.unwrap().iterator();

        for (ArgumentContext<?, ?> context : contexts) {

        }

        Object[] objects = list.stream()
            .map(Supplier::get)
            .toArray();

        Object resultOfMethod = invoker.apply(objects);

        return Result.ok(resultOfMethod);
    }

}
