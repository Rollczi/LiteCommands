package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.argument.AnnotatedParameter;
import dev.rollczi.litecommands.argument.AnnotatedParameterState;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.command.FindResult;
import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;
import dev.rollczi.litecommands.command.execute.ArgumentExecutor;
import dev.rollczi.litecommands.command.execute.ExecuteResult;
import dev.rollczi.litecommands.handle.LiteException;
import dev.rollczi.litecommands.meta.CommandMeta;
import panda.std.Blank;
import panda.std.Option;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class LiteArgumentArgumentExecutor<SENDER> implements ArgumentExecutor<SENDER> {

    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private final MethodExecutor<SENDER> executor;
    private final List<AnnotatedParameterImpl<SENDER, ?>> arguments = new ArrayList<>();

    private final CommandMeta meta = CommandMeta.create();

    private LiteArgumentArgumentExecutor(List<AnnotatedParameterImpl<SENDER, ?>> arguments, MethodExecutor<SENDER> executor) {
        this.executor = executor;
        this.arguments.addAll(arguments);
    }

    @Override
    public ExecuteResult execute(Invocation<SENDER> invocation, FindResult<SENDER> findResult) {
        if (findResult.isInvalid()) {
            Option<Object> result = findResult.getResult();

            return result
                    .map(obj -> ExecuteResult.invalid(findResult, obj))
                    .orElseGet(() -> ExecuteResult.failure(findResult));
        }

        if (findResult.isFailed()) {
            return ExecuteResult.failure(findResult);
        }

        if (!findResult.isFound()) {
            return ExecuteResult.failure(findResult);
        }

        if (Boolean.TRUE.equals(this.meta.get(CommandMeta.ASYNCHRONOUS))) {
            CompletableFuture<Object> future = CompletableFuture
                    .supplyAsync(() -> executor.execute(invocation, findResult.extractResults()), executorService);

            return ExecuteResult.success(findResult, future);
        }

        return ExecuteResult.success(findResult, executor.execute(invocation, findResult.extractResults()));
    }

    @Override
    public FindResult<SENDER> find(LiteInvocation invocation, int route, FindResult<SENDER> lastResult) {
        int currentRoute = route;
        FindResult<SENDER> currentResult = lastResult.withExecutor(this);

        for (AnnotatedParameterImpl<SENDER, ?> annotatedParameter : arguments) {
            Argument<SENDER, ?> argument = annotatedParameter.argument();
            AnnotatedParameterState<SENDER, ?> state = annotatedParameter.createState(invocation, currentRoute);

            try {
                MatchResult result = state.matchResult();

                if (result.isNotMatched()) {
                    Optional<Object> resultNoMatchedResult = result.getNoMatchedResult();

                    if (argument.isOptional() && (!isOptionStrict(annotatedParameter) || !resultNoMatchedResult.isPresent())) {
                        currentResult = currentResult.withArgument(state);
                        continue;
                    }

                    if (resultNoMatchedResult.isPresent() && (!(resultNoMatchedResult.get() instanceof Blank))) {
                        return currentResult
                                .withArgument(state)
                                .invalid(resultNoMatchedResult.get());
                    }

                    return currentResult
                            .withArgument(state)
                            .failed();
                }

                currentResult = currentResult.withArgument(state);
                currentRoute += result.getConsumed();
            }
            catch (LiteException exception) {
                return currentResult
                        .withArgument(state)
                        .invalid(exception.getResult());
            }
        }

        if (!meta.getCountValidator().valid(invocation.arguments().length - route + 1)) {
            return currentResult.invalid();
        }

        return currentResult.found();
    }

    private <A extends Annotation> boolean isOptionStrict(AnnotatedParameterImpl<SENDER, A> annotatedParameter) {
        return annotatedParameter.argument().isOptionalStrict(annotatedParameter.annotation());
    }

    @Override
    public List<AnnotatedParameter<SENDER, ?>> annotatedParameters() {
        return Collections.unmodifiableList(arguments);
    }

    @Override
    public CommandMeta meta() {
        return this.meta;
    }

    static <T> LiteArgumentArgumentExecutor<T> of(List<AnnotatedParameterImpl<T, ?>> arguments, MethodExecutor<T> executor) {
        return new LiteArgumentArgumentExecutor<>(arguments, executor);
    }

}
