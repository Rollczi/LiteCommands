package dev.rollczi.litecommands.modern.annotation.command;

import dev.rollczi.litecommands.modern.annotation.argument.ParameterArgument;
import dev.rollczi.litecommands.modern.annotation.argument.PreparedParameterArgumentImpl;
import dev.rollczi.litecommands.modern.annotation.contextual.ParameterContextual;
import dev.rollczi.litecommands.modern.argument.ArgumentParser;
import dev.rollczi.litecommands.modern.argument.ArgumentResolverRegistry;
import dev.rollczi.litecommands.modern.argument.FailedReason;
import dev.rollczi.litecommands.modern.command.PreparedArgumentIterator;
import dev.rollczi.litecommands.modern.command.CommandExecuteResult;
import dev.rollczi.litecommands.modern.command.CommandExecutor;
import dev.rollczi.litecommands.modern.argument.PreparedArgumentImpl;
import dev.rollczi.litecommands.modern.command.InvokedWrapperInfoResolver;
import dev.rollczi.litecommands.modern.wrapper.WrappedExpected;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.wrapper.WrapperFormat;
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
    private final List<ParameterContextual<?>> parameterContextuals = new ArrayList<>();
    private final List<PreparedParameterArgumentImpl<SENDER, ?>> resolvedArguments = new ArrayList<>();

    private MethodCommandExecutor(
        Method method,
        Object instance,
        List<ParameterContextual<?>> parameterContextuals,
        List<PreparedParameterArgumentImpl<SENDER, ?>> resolvedArguments
        ) {
        this.method = method;
        this.instance = instance;
        this.returnType = method.getReturnType();
        this.resolvedArguments.addAll(resolvedArguments);
        this.parameterContextuals.addAll(parameterContextuals);
    }

    @Override
    public List<PreparedArgumentImpl<SENDER, ?>> arguments() {
        return Collections.unmodifiableList(resolvedArguments);
    }

    @Override
    public List<WrapperFormat<?>> contextuals() {
        return parameterContextuals.stream()
            .map(ParameterContextual::getWrapperFormat)
            .collect(Collectors.toList());
    }

    @Override
    public Result<CommandExecuteResult, FailedReason> execute(Invocation<SENDER> invocation, InvokedWrapperInfoResolver<SENDER> wrapperInfoResolver, PreparedArgumentIterator<SENDER> cachedArgumentResolver) {
        NavigableMap<Integer, Supplier<WrappedExpected<Object>>> suppliers = new TreeMap<>();

        for (ParameterContextual<?> parameterContextual : this.parameterContextuals) {
            Result<Supplier<WrappedExpected<Object>>, FailedReason> result = provideParameterContextual(invocation, wrapperInfoResolver, parameterContextual);

            if (result.isErr()) {
                return Result.error(result.getError());
            }

            suppliers.put(parameterContextual.getParameterIndex(), result.get());
        }

        for (PreparedParameterArgumentImpl<SENDER, ?> cachedParameterArgument : this.resolvedArguments) {
            Result<Supplier<WrappedExpected<Object>>, FailedReason> result = provideResolvedArgument(invocation, cachedArgumentResolver, cachedParameterArgument
            );

            if (result.isErr()) {
                return Result.error(result.getError());
            }

            suppliers.put(cachedParameterArgument.getParameterIndex(), result.get());
        }

        if (suppliers.size() != this.method.getParameterCount()) {
            return Result.error(FailedReason.empty());
        }

        List<Object> objects = suppliers.values().stream()
            .map(Supplier::get)
            .map(WrappedExpected::unwrap)
            .collect(Collectors.toList());

        try {
            Object returnedValue = this.method.invoke(this.instance, objects.toArray());

            return Result.ok(CommandExecuteResult.success(returnedValue, this.returnType));
        } catch (Exception exception) {
            return Result.ok(CommandExecuteResult.failed(exception));
        }
    }

    private <T> Result<Supplier<WrappedExpected<Object>>, FailedReason> provideParameterContextual(Invocation<SENDER> invocation, InvokedWrapperInfoResolver<SENDER> provider, ParameterContextual<T> parameterContextual) {
        return provider.resolve(invocation, (WrapperFormat<Object>) parameterContextual.getWrapperFormat());
    }

    private <T> Result<Supplier<WrappedExpected<Object>>, FailedReason> provideResolvedArgument(Invocation<SENDER> invocation, PreparedArgumentIterator<SENDER> cachedArgumentResolver, PreparedArgumentImpl<SENDER, T> preparedArgumentImpl) {
        return cachedArgumentResolver.resolveNext(invocation, (PreparedArgumentImpl<SENDER, Object>) preparedArgumentImpl);
    }

    public static <SENDER> MethodCommandExecutor<SENDER> of(Method method, Object instance, List<ParameterContextual<?>> contextuals, ArgumentResolverRegistry<SENDER> argumentResolverRegistry) {
        List<ParameterContextual<?>> expectedContextuals = new ArrayList<>();
        List<PreparedParameterArgumentImpl<SENDER, ?>> resolvedArguments = new ArrayList<>();

        for (ParameterContextual<?> contextual : contextuals) {
            if (contextual instanceof ParameterArgument<?, ?>) {
                resolvedArguments.add(createExecutableArgument((ParameterArgument<?, ?>) contextual, argumentResolverRegistry));
            } else {
                expectedContextuals.add(contextual);
            }
        }

        method.setAccessible(true);

        return new MethodCommandExecutor<>(method, instance, expectedContextuals, resolvedArguments);
    }

    private static <SENDER, D extends Annotation, E, ARGUMENT extends ParameterArgument<D, E>> PreparedParameterArgumentImpl<SENDER, E> createExecutableArgument(ARGUMENT argument, ArgumentResolverRegistry<SENDER> argumentResolverRegistry) {
        Optional<ArgumentParser<SENDER, E, ARGUMENT>> resolver = argumentResolverRegistry.getResolver(ArgumentResolverRegistry.IndexKey.from(argument));

        if (!resolver.isPresent()) {
            throw new IllegalStateException("Cannot find resolver for " + argument);
        }

        return PreparedParameterArgumentImpl.create(argument, resolver.get());
    }

}
