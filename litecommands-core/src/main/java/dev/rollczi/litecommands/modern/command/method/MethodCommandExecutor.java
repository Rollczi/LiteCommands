package dev.rollczi.litecommands.modern.command.method;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.CommandExecutor;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResult;
import dev.rollczi.litecommands.modern.command.argument.invocation.FailedReason;
import dev.rollczi.litecommands.modern.command.argument.invocation.SuccessfulResult;
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
    public <SENDER> Result<Object, FailedReason> execute(ArgumentResultCollector<SENDER> collector) {
        List<Supplier<?>> list = new ArrayList<>();

        for (ArgumentResultContext<?, ?> resultContext : collector.getResults()) {
            ArgumentResult<?> result = resultContext.getResult();

            if (result.isFailed()) {
                return Result.error(result.getFailedReason());
            }

            SuccessfulResult<?> successfulResult = result.getSuccessfulResult();

            list.add(successfulResult.getParsedArgument());
        }

        Object[] objects = list.stream()
            .map(Supplier::get)
            .toArray();

        Object resultOfMethod = invoker.apply(objects);

        return Result.ok(resultOfMethod);
    }

}
