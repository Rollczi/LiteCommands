package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.argument.AnnotatedParameter;
import dev.rollczi.litecommands.argument.AnnotatedParameterState;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.command.execute.ExecuteResult;
import dev.rollczi.litecommands.command.FindResult;
import dev.rollczi.litecommands.command.amount.AmountValidator;
import dev.rollczi.litecommands.command.execute.ArgumentExecutor;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;
import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.command.sugesstion.Suggestion;
import dev.rollczi.litecommands.handle.LiteException;
import panda.std.Option;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class LiteArgumentArgumentExecutor<SENDER> implements ArgumentExecutor<SENDER> {

    private final MethodExecutor<SENDER> executor;
    private final List<AnnotatedParameterImpl<SENDER, ?>> arguments = new ArrayList<>();
    private final AmountValidator amountValidator;

    private final CommandMeta meta = new LiteCommandMeta();

    private LiteArgumentArgumentExecutor(List<AnnotatedParameterImpl<SENDER, ?>> arguments, MethodExecutor<SENDER> executor, AmountValidator amountValidator) {
        this.executor = executor;
        this.amountValidator = amountValidator;
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
                    if (argument.isOptional()) {
                        currentResult = currentResult.withArgument(state);
                        continue;
                    }

                    if (result.getNoMatchedResult().isPresent()) {
                        return currentResult
                                .withArgument(state)
                                .invalid(result.getNoMatchedResult().get());
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

        if (!amountValidator.valid(invocation.arguments().length - route + 1)) {
            return currentResult.invalid();
        }

        return currentResult.found();
    }

    @Override
    public List<Argument<SENDER, ?>> arguments() {
        return this.arguments.stream()
                .map(AnnotatedParameterImpl::argument)
                .collect(Collectors.toList());
    }

    @Override
    public List<AnnotatedParameter<SENDER, ?>> annotatedParameters() {
        return Collections.unmodifiableList(arguments);
    }

    @Override
    public CommandMeta meta() {
        return this.meta;
    }

    @Override
    public AmountValidator amountValidator() {
        return this.amountValidator;
    }

    static <T> LiteArgumentArgumentExecutor<T> of(List<AnnotatedParameterImpl<T, ?>> arguments, MethodExecutor<T> executor, AmountValidator amountValidator) {
        return new LiteArgumentArgumentExecutor<>(arguments, executor, amountValidator);
    }

    @Override
    public List<Suggestion> firstSuggestions(LiteInvocation invocation) {
        if (arguments.isEmpty()) {
            return Collections.emptyList();
        }

        AnnotatedParameterImpl<SENDER, ?> parameter = arguments.get(0);

        return parameter.extractSuggestion(invocation);
    }

}
