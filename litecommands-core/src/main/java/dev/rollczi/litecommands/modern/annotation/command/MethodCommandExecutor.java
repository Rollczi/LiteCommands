package dev.rollczi.litecommands.modern.annotation.command;

import dev.rollczi.litecommands.modern.annotation.contextual.ParameterContextual;
import dev.rollczi.litecommands.modern.argument.FailedReason;
import dev.rollczi.litecommands.modern.command.CommandExecuteResult;
import dev.rollczi.litecommands.modern.command.CommandExecutor;
import dev.rollczi.litecommands.modern.command.CommandExecutorKey;
import dev.rollczi.litecommands.modern.contextual.ExpectedContextual;
import dev.rollczi.litecommands.modern.contextual.warpped.ExpectedContextualConverter;
import dev.rollczi.litecommands.modern.contextual.warpped.WrappedExpectedContextual;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import panda.std.Result;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

class MethodCommandExecutor implements CommandExecutor {

    private final Method method;
    private final Object instance;
    private final Class<?> returnType;
    private final List<ParameterContextual<?>> expectedContextual = new ArrayList<>();
    private final CommandExecutorKey executorKey;

    MethodCommandExecutor(Method method, List<ParameterContextual<?>> expectedContextual, Object instance) {
        this.method = method;
        this.instance = instance;
        this.returnType = method.getReturnType();
        this.expectedContextual.addAll(expectedContextual);
        this.executorKey = new CommandExecutorKey(Collections.unmodifiableList(expectedContextual));
    }

    @Override
    public CommandExecutorKey getKey() {
        return this.executorKey;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <SENDER> Result<CommandExecuteResult, FailedReason> execute(Invocation<SENDER> invocation, ExpectedContextualConverter<SENDER> provider) {
        List<Supplier<WrappedExpectedContextual<Object>>> suppliers = new ArrayList<>();

        for (ParameterContextual<?> parameterContextual : this.expectedContextual) {
            Result<Supplier<WrappedExpectedContextual<Object>>, FailedReason> result = provider.provide(invocation, (ExpectedContextual<Object>) parameterContextual);

            if (result.isErr()) {
                return Result.error(result.getError());
            }

            suppliers.add(result.get());
        }

        List<Object> objects = suppliers.stream()
            .map(Supplier::get)
            .map(WrappedExpectedContextual::unwrap)
            .collect(Collectors.toList());

        try {
            Object returnedValue = this.method.invoke(this.instance, objects.toArray());

            return Result.ok(CommandExecuteResult.success(returnedValue, this.returnType));
        } catch (Exception exception) {
            return Result.ok(CommandExecuteResult.failed(exception));
        }
    }

}
