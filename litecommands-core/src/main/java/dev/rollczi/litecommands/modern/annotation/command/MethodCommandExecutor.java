package dev.rollczi.litecommands.modern.annotation.command;

import dev.rollczi.litecommands.modern.annotation.argument.ParameterArgument;
import dev.rollczi.litecommands.modern.annotation.contextual.ParameterContextual;
import dev.rollczi.litecommands.modern.argument.ArgumentResolver;
import dev.rollczi.litecommands.modern.argument.ArgumentResolverRegistry;
import dev.rollczi.litecommands.modern.argument.FailedReason;
import dev.rollczi.litecommands.modern.command.CommandExecuteResult;
import dev.rollczi.litecommands.modern.command.CommandExecutor;
import dev.rollczi.litecommands.modern.command.CommandExecutorKey;
import dev.rollczi.litecommands.modern.command.ExecutableArgument;
import dev.rollczi.litecommands.modern.contextual.ExpectedContextual;
import dev.rollczi.litecommands.modern.contextual.warpped.ExpectedContextualConverter;
import dev.rollczi.litecommands.modern.contextual.warpped.WrappedExpectedContextual;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import panda.std.Result;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

class MethodCommandExecutor<SENDER> implements CommandExecutor<SENDER> {

    private final Method method;
    private final Object instance;
    private final Class<?> returnType;
    private final List<ParameterContextual<?>> expectedContextuals = new ArrayList<>();
    private final List<ExecutableArgument<SENDER, ?, ?, ? extends ParameterArgument<?, ?>>> executableArguments = new ArrayList<>();
    private final CommandExecutorKey executorKey;

    private MethodCommandExecutor(
        Method method,
        Object instance,
        List<ExpectedContextual<?>> allExpectedContextual,
        List<ParameterContextual<?>> expectedContextuals,
        List<ExecutableArgument<SENDER, ?, ?, ? extends ParameterArgument<?, ?>>> executableArguments
        ) {
        this.method = method;
        this.instance = instance;
        this.returnType = method.getReturnType();
        this.executableArguments.addAll(executableArguments);
        this.expectedContextuals.addAll(expectedContextuals);
        this.executorKey = new CommandExecutorKey(Collections.unmodifiableList(allExpectedContextual));
    }

    @Override
    public CommandExecutorKey getKey() {
        return this.executorKey;
    }

    @Override
    public List<ExecutableArgument<SENDER, ?, ?, ?>> arguments() {
        return Collections.unmodifiableList(executableArguments);
    }

    @Override
    public List<ExpectedContextual<?>> contextuals() {
        return Collections.unmodifiableList(executableArguments);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Result<CommandExecuteResult, FailedReason> execute(Invocation<SENDER> invocation, ExpectedContextualConverter<SENDER> provider) {
        NavigableMap<Integer, Supplier<WrappedExpectedContextual<Object>>> suppliers = new TreeMap<>();

        for (ParameterContextual<?> parameterContextual : this.expectedContextuals) {
            Result<Supplier<WrappedExpectedContextual<Object>>, FailedReason> result = provider.provide(invocation, (ExpectedContextual<Object>) parameterContextual);

            if (result.isErr()) {
                return Result.error(result.getError());
            }

            suppliers.put(parameterContextual.getParameterIndex(), result.get());
        }

        for (ExecutableArgument<SENDER, ?, ?, ? extends ParameterArgument<?, ?>> executableArgument : this.executableArguments) {
            Result<Supplier<WrappedExpectedContextual<Object>>, FailedReason> result = provider.provide(invocation, (ExpectedContextual<Object>) executableArgument);

            if (result.isErr()) {
                return Result.error(result.getError());
            }

            suppliers.put(executableArgument.getContextual().getParameterIndex(), result.get());
        }

        if (suppliers.size() != this.method.getParameterCount()) {
            return Result.error(FailedReason.empty());
        }

        List<Object> objects = suppliers.values().stream()
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

    public static <SENDER> MethodCommandExecutor<SENDER> of(Method method, Object instance, List<ParameterContextual<?>> contextuals, ArgumentResolverRegistry<SENDER> argumentResolverRegistry) {
        List<ParameterContextual<?>> expectedContextuals = new ArrayList<>();
        List<ExecutableArgument<SENDER, ?, ?, ? extends ParameterArgument<?, ?>>> executableArguments = new ArrayList<>();

        for (ParameterContextual<?> contextual : contextuals) {
            if (contextual instanceof ParameterArgument<?, ?>) {
                executableArguments.add(createExecutableArgument((ParameterArgument<?, ?>) contextual, argumentResolverRegistry));
            } else {
                expectedContextuals.add(contextual);
            }
        }

        List<ExpectedContextual<?>> allExpectedContextual = new ArrayList<>();
        allExpectedContextual.addAll(expectedContextuals);
        allExpectedContextual.addAll(executableArguments);

        method.setAccessible(true);

        return new MethodCommandExecutor<>(method, instance, allExpectedContextual, expectedContextuals, executableArguments);
    }

    private static <SENDER, D extends Annotation, E, C extends ParameterArgument<D, E>> ExecutableArgument<SENDER, D, E, C> createExecutableArgument(C contextual, ArgumentResolverRegistry<SENDER> argumentResolverRegistry) {
        Optional<ArgumentResolver<SENDER, D, E, C>> resolver = argumentResolverRegistry.getResolver(ArgumentResolverRegistry.IndexKey.from(contextual));

        if (!resolver.isPresent()) {
            throw new IllegalStateException("Cannot find resolver for " + contextual);
        }

        return new ExecutableArgument<>(contextual, resolver.get());
    }

}
