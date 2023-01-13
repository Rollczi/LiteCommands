package dev.rollczi.litecommands.modern.extension.annotation.method;

import dev.rollczi.litecommands.modern.command.CommandExecuteResult;
import dev.rollczi.litecommands.modern.command.Invocation;
import dev.rollczi.litecommands.modern.command.CommandExecutor;
import dev.rollczi.litecommands.modern.command.argument.invocation.FailedReason;
import dev.rollczi.litecommands.modern.command.argument.invocation.WrappedArgumentProvider;
import dev.rollczi.litecommands.modern.command.contextual.ExpectedContextual;
import dev.rollczi.litecommands.modern.command.contextual.warpped.WrappedArgumentWrapper;
import panda.std.Result;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

class MethodCommandExecutor implements CommandExecutor {

    private final Method method;
    private final Object instance;
    private final Class<?> returnType;
    private final List<ExpectedContextual<?>> expectedContextual = new ArrayList<>();

    MethodCommandExecutor(Method method, List<ExpectedContextual<?>> expectedContextual, Object instance) {
        this.method = method;
        this.instance = instance;
        this.expectedContextual.addAll(expectedContextual);
        this.returnType = method.getReturnType();
    }

    @Override
    public <SENDER> Result<CommandExecuteResult, FailedReason> execute(Invocation<SENDER> invocation, WrappedArgumentProvider<SENDER> provider) {
        List<Supplier<WrappedArgumentWrapper<Object>>> suppliers = new ArrayList<>();

        for (ExpectedContextual<?> contextual : expectedContextual) {
            Result<Supplier<WrappedArgumentWrapper<Object>>, FailedReason> result = provider.provide(invocation, (ExpectedContextual<Object>) contextual);

            if (result.isErr()) {
                return Result.error(result.getError());
            }

            suppliers.add(result.get());
        }

        List<Object> objects = suppliers.stream()
            .map(Supplier::get)
            .map(WrappedArgumentWrapper::unwrap)
            .collect(Collectors.toList());

        try {
            Object returnedValue = method.invoke(instance, objects.toArray());

            return Result.ok(CommandExecuteResult.success(returnedValue, returnType));
        } catch (Exception exception) {
            return Result.ok(CommandExecuteResult.failed(exception));
        }
    }

}
