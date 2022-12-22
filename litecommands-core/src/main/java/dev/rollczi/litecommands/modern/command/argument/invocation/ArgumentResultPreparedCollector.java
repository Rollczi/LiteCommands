package dev.rollczi.litecommands.modern.command.argument.invocation;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;

import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

public class ArgumentResultPreparedCollector<SENDER, DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> {

    private final Invocation<SENDER> invocation;
    private final NavigableMap<Integer, ArgumentResultContext<?, ?>> results;
    private final int lastResolvedRawArgument;
    private final CONTEXT currentContext;

    ArgumentResultPreparedCollector(Invocation<SENDER> invocation, NavigableMap<Integer, ArgumentResultContext<?, ?>> results, int lastResolvedRawArgument, CONTEXT currentContext) {
        this.invocation = invocation;
        this.results = results;
        this.lastResolvedRawArgument = lastResolvedRawArgument;
        this.currentContext = currentContext;
    }

    public ArgumentResultCollector<SENDER> collect(int requiredArguments, ArgumentInvocationParserInternal<SENDER, DETERMINANT, EXPECTED, CONTEXT> argumentInvocationParserInternal) {
        ArgumentResultContext<?, ?> lastResult = results.get(lastResolvedRawArgument);

        if (lastResult != null && lastResult.getResult().isFailed()) {
            throw new IllegalStateException("Cannot resolve arguments when last argument is failed");
        }

        List<String> rawArguments = this.invocation.argumentsList();
        int lastRequiredArgument = lastResolvedRawArgument + requiredArguments;

        if (lastRequiredArgument > rawArguments.size()) {
            ArgumentResult<EXPECTED> result = ArgumentResult.failure();;
            ArgumentResultContext<DETERMINANT, EXPECTED> resultContext = new ArgumentResultContext<>(result, this.currentContext);
            NavigableMap<Integer, ArgumentResultContext<?, ?>> newResults = this.copyAndPut(resultContext);

            return new ArgumentResultCollector<>(this.invocation, newResults, this.lastResolvedRawArgument);
        }

        List<String> arguments = rawArguments.subList(this.lastResolvedRawArgument, lastRequiredArgument);

        ArgumentResult<EXPECTED> result = argumentInvocationParserInternal.parse(this.invocation, this.currentContext, arguments);
        ArgumentResultContext<DETERMINANT, EXPECTED> resultContext = new ArgumentResultContext<>(result, this.currentContext);
        NavigableMap<Integer, ArgumentResultContext<?, ?>> newResults = this.copyAndPut(resultContext);

        if (result.isFailed()) {
            return new ArgumentResultCollector<>(this.invocation, newResults, this.lastResolvedRawArgument);
        }

        SuccessfulResult<EXPECTED> successfulResult = result.getSuccessfulResult();

        return new ArgumentResultCollector<>(this.invocation, newResults, successfulResult.getConsumedRawArguments() + this.lastResolvedRawArgument);
    }

    private NavigableMap<Integer, ArgumentResultContext<?, ?>> copyAndPut(ArgumentResultContext<?, ?> result) {
        NavigableMap<Integer, ArgumentResultContext<?, ?>> newResults = new TreeMap<>(this.results);
        newResults.put(this.lastResolvedRawArgument, result);

        return newResults;
    }


}
