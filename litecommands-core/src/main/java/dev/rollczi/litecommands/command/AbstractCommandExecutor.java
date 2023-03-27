package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.argument.PreparedArgument;
import dev.rollczi.litecommands.argument.PreparedArgumentResult;
import dev.rollczi.litecommands.invalid.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.range.Range;
import org.jetbrains.annotations.Nullable;
import panda.std.Option;
import panda.std.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractCommandExecutor<SENDER, ARGUMENT extends PreparedArgument<SENDER, ?>> implements CommandExecutor<SENDER> {

    protected final List<ARGUMENT> arguments = new ArrayList<>();
    protected final Range rangeOfArguments;
    protected final CommandMeta meta = CommandMeta.create();

    protected AbstractCommandExecutor(Collection<? extends ARGUMENT> arguments) {
        int min = 0;
        int max = 0;

        for (ARGUMENT argument : arguments) {
            Range range = argument.getRange();

            min += range.getMin();
            max = range.getMax() == Integer.MAX_VALUE ? Integer.MAX_VALUE : max + range.getMax();
        }

        this.rangeOfArguments = new Range(min, max);
        this.arguments.addAll(arguments);
    }

    @Override
    public CommandMeta getMeta() {
        return meta;
    }

    @Override
    public List<PreparedArgument<SENDER, ?>> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    @Override
    public CommandExecutorMatchResult match(Invocation<SENDER> invocation, Context context) {
        int rawArgumentAmount = invocation.argumentsList().size() - context.getRouteBeforeArguments();

        if (this.rangeOfArguments.isAbove(rawArgumentAmount)) {
            return CommandExecutorMatchResult.failed(InvalidUsage.Cause.TOO_MANY_ARGUMENTS);
        }

        if (this.rangeOfArguments.isBelow(rawArgumentAmount)) {
            return CommandExecutorMatchResult.failed(InvalidUsage.Cause.TOO_FEW_ARGUMENTS);
        }

        List<Pair<ARGUMENT, PreparedArgumentResult.Success<?>>> results = new ArrayList<>();

        ArgumentResolverContext<?> currentContext = ArgumentResolverContext.create(context.getRouteBeforeArguments());

        for (ARGUMENT argument : this.arguments) {
            currentContext = resolveNextArgument(invocation, (PreparedArgument<SENDER, ?>) argument, currentContext);
            PreparedArgumentResult<?> result = currentContext.getLastArgumentResult()
                .orThrow(() -> new IllegalStateException("Argument resolver context must have last argument result"));

            if (result.isFailed()) { // TODO add option disable strict mode and use empty wrapper
                return CommandExecutorMatchResult.failed(result.getFailedReason());
            }

            results.add(Pair.of(argument, result.getSuccess()));
        }

        return this.match(results);
    }

    protected abstract CommandExecutorMatchResult match(List<Pair<ARGUMENT, PreparedArgumentResult.Success<?>>> results);

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

    public static class ArgumentResolverContext<E> {

        private final int lastResolvedRawArgument;
        private final @Nullable PreparedArgumentResult<E> lastArgumentResult;

        private ArgumentResolverContext(int lastResolvedRawArgument, @Nullable PreparedArgumentResult<E> lastArgumentResult) {
            this.lastResolvedRawArgument = lastResolvedRawArgument;
            this.lastArgumentResult = lastArgumentResult;
        }

        public int getLastResolvedRawArgument() {
            return this.lastResolvedRawArgument;
        }

        public Option<PreparedArgumentResult<E>> getLastArgumentResult() {
            return Option.of(this.lastArgumentResult);
        }

        public <T> ArgumentResolverContext<T> with(int consumed, PreparedArgumentResult<T> lastArgumentResult) {
            return new ArgumentResolverContext<>(this.lastResolvedRawArgument + consumed, lastArgumentResult);
        }

        public <T> ArgumentResolverContext<T> withFailure(PreparedArgumentResult<T> argumentResult) {
            return this.with(0, argumentResult);
        }

        public static <E> ArgumentResolverContext<E> create(int childIndex) {
            return new ArgumentResolverContext<>(childIndex, null);
        }

    }

}
