package dev.rollczi.litecommands.annotations.command;

import dev.rollczi.litecommands.argument.ArgumentResolverContext;
import dev.rollczi.litecommands.argument.FailedReason;
import dev.rollczi.litecommands.argument.PreparedArgument;
import dev.rollczi.litecommands.argument.PreparedArgumentResult;
import dev.rollczi.litecommands.command.CommandExecuteResult;
import dev.rollczi.litecommands.command.CommandExecutor;
import dev.rollczi.litecommands.command.CommandExecutorMatchResult;
import dev.rollczi.litecommands.invalid.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.wrapper.WrappedExpected;
import panda.std.Option;
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
    private final Range rangeOfArguments;

    MethodCommandExecutor(
        Method method,
        Object instance,
        List<ParameterPreparedArgument<SENDER, ?>> preparedArguments
    ) {
        this.method = method;
        this.instance = instance;
        this.returnType = method.getReturnType();
        this.preparedArguments.addAll(preparedArguments);

        int min = 0;
        int max = 0;

        for (ParameterPreparedArgument<SENDER, ?> argument : this.preparedArguments) {
            Range range = argument.getRange();

            min += range.getMin();
            max = range.getMax() == Integer.MAX_VALUE ? Integer.MAX_VALUE : max + range.getMax();
        }

        this.rangeOfArguments = new Range(min, max);
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
    public CommandExecutorMatchResult match(Invocation<SENDER> invocation, ArgumentResolverContext<?> rootContext) {
        int rawArgumentAmount = invocation.argumentsList().size() - rootContext.getLastResolvedRawArgument();

        if (this.rangeOfArguments.isAbove(rawArgumentAmount)) {
            return CommandExecutorMatchResult.failed(InvalidUsage.Cause.TOO_MANY_ARGUMENTS);
        }

        if (this.rangeOfArguments.isBelow(rawArgumentAmount)) {
            return CommandExecutorMatchResult.failed(InvalidUsage.Cause.TOO_FEW_ARGUMENTS);
        }


        NavigableMap<Integer, Supplier<WrappedExpected<Object>>> suppliers = new TreeMap<>();
        ArgumentResolverContext<?> currentContext = rootContext;

        for (ParameterPreparedArgument<SENDER, ?> argument : preparedArguments) {
            currentContext = resolveNextArgument(invocation, argument, currentContext);

            Result<Supplier<WrappedExpected<Object>>, FailedReason> result = this.unpackContext((ArgumentResolverContext<Object>) currentContext);

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

    private Result<Supplier<WrappedExpected<Object>>, FailedReason> unpackContext(ArgumentResolverContext<Object> resolverContext) {
        Option<PreparedArgumentResult<Object>> result = resolverContext.getLastArgumentResult();

        if (result.isEmpty()) {
            throw new IllegalStateException();
        }

        PreparedArgumentResult<Object> argumentResult = result.get();

        if (argumentResult.isFailed()) { // TODO add option disable strict mode and use empty wrapper
            Option<WrappedExpected<Object>> wrapper = Option.none();

            if (wrapper.isEmpty()) {
                return Result.error(argumentResult.getFailedReason());
            }

            return Result.ok(wrapper::get);
        }

        PreparedArgumentResult.Success<Object> successfulResult = argumentResult.getSuccess();

        return Result.ok(successfulResult.getWrappedExpected());
    }

    public <EXPECTED> ArgumentResolverContext<EXPECTED> resolveNextArgument(
        Invocation<SENDER> invocation,
        PreparedArgument<SENDER, EXPECTED> preparedArgument,
        ArgumentResolverContext<?> resolverContext
    ) {

        Option<? extends PreparedArgumentResult<?>> lastResult = resolverContext.getLastArgumentResult();

        if (lastResult.isPresent() && lastResult.get().isFailed()) {
            throw new IllegalStateException("Cannot resolve arguments when last argument is failed");
        }

        Range range = preparedArgument.getRange();
        int lastResolvedRawArgument = resolverContext.getLastResolvedRawArgument();

        List<String> rawArguments = invocation.argumentsList();
        int minArguments = range.getMin();
        int maxArguments = range.getMax() == Integer.MAX_VALUE
            ? rawArguments.size()
            : lastResolvedRawArgument + range.getMax();

        maxArguments = Math.min(maxArguments, rawArguments.size());

        if (minArguments > rawArguments.size()) {
            return resolverContext.withFailure(PreparedArgumentResult.failed(InvalidUsage.Cause.TOO_FEW_ARGUMENTS));
        }

        List<String> arguments = rawArguments.subList(lastResolvedRawArgument, maxArguments);
        PreparedArgumentResult<EXPECTED> result = preparedArgument.resolve(invocation, arguments);

        if (result.isFailed()) {
            return resolverContext.withFailure(result);
        }

        PreparedArgumentResult.Success<EXPECTED> success = result.getSuccess();

        return resolverContext.with(success.getConsumedRawArguments(), result);
    }

}
