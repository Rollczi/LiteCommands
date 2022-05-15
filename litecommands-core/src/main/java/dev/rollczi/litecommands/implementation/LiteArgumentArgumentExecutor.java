package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.argument.AnnotatedParameter;
import dev.rollczi.litecommands.argument.AnnotatedParameterState;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.command.execute.ExecuteResult;
import dev.rollczi.litecommands.command.FindResult;
import dev.rollczi.litecommands.command.amount.AmountValidator;
import dev.rollczi.litecommands.command.execute.ArgumentExecutor;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;
import dev.rollczi.litecommands.command.sugesstion.Suggestion;
import dev.rollczi.litecommands.handle.LiteException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class LiteArgumentArgumentExecutor implements ArgumentExecutor {

    private final MethodExecutor executor;
    private final List<AnnotatedParameterImpl<?>> arguments = new ArrayList<>();
    private final AmountValidator amountValidator;

    private LiteArgumentArgumentExecutor(List<AnnotatedParameterImpl<?>> arguments, MethodExecutor executor, AmountValidator amountValidator) {
        this.executor = executor;
        this.amountValidator = amountValidator;
        this.arguments.addAll(arguments);
    }

    @Override
    public ExecuteResult execute(LiteInvocation invocation, FindResult findResult) {
        if (findResult.isInvalid()) {
            Optional<Object> result = findResult.getResult();

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
    public FindResult find(LiteInvocation invocation, int route, FindResult lastResult) {
        int currentRoute = route;
        FindResult currentResult = lastResult.withExecutor(this);

        for (AnnotatedParameterImpl<?> annotatedParameter : arguments) {
            Argument<?> argument = annotatedParameter.argument();
            AnnotatedParameterState<?> state = annotatedParameter.createState(invocation, currentRoute);

            try {
                MatchResult result = state.matchResult();

                if (result.isNotMatched()) {
                    if (argument.isOptional()) {
                        currentResult = currentResult.withArgument(state);
                        continue;
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
    public List<Argument<?>> arguments() {
        return this.arguments.stream()
                .map(AnnotatedParameterImpl::argument)
                .collect(Collectors.toList());
    }

    @Override
    public List<AnnotatedParameter<?>> annotatedParameters() {
        return Collections.unmodifiableList(arguments);
    }

    @Override
    public AmountValidator amountValidator() {
        return this.amountValidator;
    }

    static LiteArgumentArgumentExecutor of(List<AnnotatedParameterImpl<?>> arguments, MethodExecutor executor, AmountValidator amountValidator) {
        return new LiteArgumentArgumentExecutor(arguments, executor, amountValidator);
    }

    @Override
    public List<Suggestion> firstSuggestions(LiteInvocation invocation) {
        if (arguments.isEmpty()) {
            return Collections.emptyList();
        }

        AnnotatedParameterImpl<?> parameter = arguments.get(0);

        return parameter.extractSuggestion(invocation);
    }

}
