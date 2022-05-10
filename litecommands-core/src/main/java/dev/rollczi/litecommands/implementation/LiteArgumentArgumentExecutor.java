package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.command.Suggestion;
import dev.rollczi.litecommands.command.ExecuteResult;
import dev.rollczi.litecommands.command.FindResult;
import dev.rollczi.litecommands.command.amount.AmountValidator;
import dev.rollczi.litecommands.command.execute.ArgumentExecutor;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;
import dev.rollczi.litecommands.handle.LiteException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class LiteArgumentArgumentExecutor implements ArgumentExecutor {

    private final MethodExecutor executor;
    private final List<AnnotatedArgument<?>> arguments = new ArrayList<>();
    private final AmountValidator amountValidator;

    private LiteArgumentArgumentExecutor(List<AnnotatedArgument<?>> arguments, MethodExecutor executor, AmountValidator amountValidator) {
        this.executor = executor;
        this.amountValidator = amountValidator;
        this.arguments.addAll(arguments);
    }

    @Override
    public ExecuteResult execute(LiteInvocation invocation, FindResult findResult) {
        Optional<Object> result = findResult.getInvalidResult();

        if (findResult.isInvalid()) {
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
        FindResult currentResult = lastResult.withExecutor(this, new ArrayList<>(arguments));

        for (AnnotatedArgument<?> annotatedArgument : arguments) {
            Argument<?> argument = annotatedArgument.argument();
            List<Suggestion> suggestion = annotatedArgument.complete(invocation);

            try {
                MatchResult result = annotatedArgument.match(invocation, currentRoute);

                if (result.isNotMatched()) {
                    if (argument.isOptional()) {
                        currentResult = currentResult.withArgument(argument, argument.getDefault(), suggestion, false);
                        continue;
                    }

                    Optional<Object> noMatchedResult = result.getNoMatchedResult();

                    if (noMatchedResult.isPresent()) {
                        return currentResult.invalidArgument(argument, suggestion, noMatchedResult.get());
                    }

                    return currentResult.failedArgument(argument, suggestion);
                }

                currentResult = currentResult.withArgument(argument, result.getResults(), suggestion, true);
                currentRoute += result.getConsumed();
            }
            catch (LiteException exception) {
                return currentResult.invalidArgument(argument, suggestion, exception.getValue());
            }
        }

        if (!amountValidator.valid(invocation.arguments().length - route + 1)) {
            return currentResult.invalid();
        }

        return currentResult.markAsFound();
    }

    @Override
    public List<Argument<?>> arguments() {
        return this.arguments.stream()
                .map(AnnotatedArgument::argument)
                .collect(Collectors.toList());
    }

    @Override
    public AmountValidator amountValidator() {
        return this.amountValidator;
    }

    static LiteArgumentArgumentExecutor of(List<AnnotatedArgument<?>> arguments, MethodExecutor executor, AmountValidator amountValidator) {
        return new LiteArgumentArgumentExecutor(arguments, executor, amountValidator);
    }

}
