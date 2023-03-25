package dev.rollczi.litecommands.annotations.command;

import dev.rollczi.litecommands.argument.FailedReason;
import dev.rollczi.litecommands.argument.PreparedArgument;
import dev.rollczi.litecommands.command.CommandExecuteResult;
import dev.rollczi.litecommands.command.CommandExecutor;
import dev.rollczi.litecommands.command.CommandExecutorMatchResult;
import dev.rollczi.litecommands.command.PreparedArgumentIterator;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.wrapper.WrappedExpected;
import panda.std.Result;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Supplier;

class MethodCommandExecutor<SENDER> implements CommandExecutor<SENDER> {

    private final Method method;
    private final Object instance;
    private final Class<?> returnType;
    private final List<ParameterPreparedArgument<SENDER, ?>> preparedArguments = new ArrayList<>();
    private final CommandMeta meta = CommandMeta.create();

    MethodCommandExecutor(
        Method method,
        Object instance,
        List<ParameterPreparedArgument<SENDER, ?>> preparedArguments
        ) {
        this.method = method;
        this.instance = instance;
        this.returnType = method.getReturnType();
        this.preparedArguments.addAll(preparedArguments);
    }

    @Override
    public List<PreparedArgument<SENDER, ?>> getArguments() {
        return Collections.unmodifiableList(preparedArguments);
    }

    @Override
    public CommandMeta getMeta() {
        return meta;
    }

    @Override
    public CommandExecutorMatchResult match(Invocation<SENDER> invocation, PreparedArgumentIterator<SENDER> cachedArgumentResolver) {
        NavigableMap<Integer, Supplier<WrappedExpected<Object>>> suppliers = new TreeMap<>();

        for (ParameterPreparedArgument<SENDER, ?> argument : preparedArguments) {
            Result<Supplier<WrappedExpected<Object>>, FailedReason> result = this.resolve(invocation, cachedArgumentResolver, argument);

            if (result.isErr()) {
                return CommandExecutorMatchResult.failed(result.getError());
            }

            suppliers.put(argument.getParameterIndex(), result.get());
        }

        if (suppliers.size() != this.method.getParameterCount()) {
            return CommandExecutorMatchResult.failed(new IllegalStateException("Not all parameters are resolved"));
        }

        Object[] objects = suppliers.values().stream()
            .map(Supplier::get)
            .map(WrappedExpected::unwrap)
            .toArray();

        return CommandExecutorMatchResult.success(() -> {
            try {
                this.method.setAccessible(true);

                return CommandExecuteResult.success(this.method.invoke(this.instance, objects), this.returnType);
            }
            catch (Exception exception) {
                return CommandExecuteResult.failed(exception);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private <T> Result<Supplier<WrappedExpected<Object>>, FailedReason> resolve(Invocation<SENDER> invocation, PreparedArgumentIterator<SENDER> preparedArgumentIterator, ParameterPreparedArgument<SENDER, T> parameterPreparedArgument) {
        return preparedArgumentIterator.resolveNext(invocation, (PreparedArgument<SENDER, Object>) parameterPreparedArgument);
    }

}
