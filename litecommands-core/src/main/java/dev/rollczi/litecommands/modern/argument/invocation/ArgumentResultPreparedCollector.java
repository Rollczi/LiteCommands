package dev.rollczi.litecommands.modern.argument.invocation;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.modern.argument.ArgumentContext;

import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

public class ArgumentResultPreparedCollector<SENDER, DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> {

    private final Invocation<SENDER> invocation;
    private final NavigableMap<Integer, ArgumentResult<?>> results;
    private final int lastResolvedRawArgument;
    private final CONTEXT currentContextBox;

    ArgumentResultPreparedCollector(Invocation<SENDER> invocation, NavigableMap<Integer, ArgumentResult<?>> results, int lastResolvedRawArgument, CONTEXT currentContextBox) {
        this.invocation = invocation;
        this.results = results;
        this.lastResolvedRawArgument = lastResolvedRawArgument;
        this.currentContextBox = currentContextBox;
    }

    public ArgumentResultCollector<SENDER> collect(int requiredArguments, ArgumentParser<SENDER, DETERMINANT, EXPECTED, CONTEXT> argumentParser) {
        ArgumentResult<?> lastResult = results.get(lastResolvedRawArgument);

        if (lastResult != null && lastResult.isFailed()) {
            throw new IllegalStateException("Cannot resolve arguments when last argument is failed");
        }

        List<String> rawArguments = this.invocation.argumentsList();
        int lastRequiredArgument = lastResolvedRawArgument + requiredArguments;

        if (lastRequiredArgument > rawArguments.size()) {
            ArgumentResult<EXPECTED> result = ArgumentResult.failure(FailedReason.empty());
            NavigableMap<Integer, ArgumentResult<?>> newResults = this.copyAndPut(result);

            return new ArgumentResultCollector<>(this.invocation, newResults, this.lastResolvedRawArgument);
        }

        List<String> arguments = rawArguments.subList(this.lastResolvedRawArgument, lastRequiredArgument);

        ArgumentResult<EXPECTED> result = argumentParser.parse(this.invocation, currentContextBox, arguments);
        NavigableMap<Integer, ArgumentResult<?>> newResults = this.copyAndPut(result);

        if (result.isFailed()) {
            return new ArgumentResultCollector<>(this.invocation, newResults, this.lastResolvedRawArgument);
        }

        SuccessfulResult<EXPECTED> successfulResult = result.getSuccessfulResult();

        return new ArgumentResultCollector<>(this.invocation, newResults, successfulResult.getConsumedRawArguments() + this.lastResolvedRawArgument);
    }

    private NavigableMap<Integer, ArgumentResult<?>> copyAndPut(ArgumentResult<?> result) {
        NavigableMap<Integer, ArgumentResult<?>> newResults = new TreeMap<>(this.results);
        newResults.put(this.lastResolvedRawArgument, result);

        return newResults;
    }


}
