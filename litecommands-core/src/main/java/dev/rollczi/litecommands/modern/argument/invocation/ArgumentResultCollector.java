package dev.rollczi.litecommands.modern.argument.invocation;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.modern.argument.ArgumentContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

public class ArgumentResultCollector<SENDER> {

    private final Invocation<SENDER> invocation;
    private final NavigableMap<Integer, ArgumentResult<?>> results;
    private final int lastResolvedRawArgument;

    ArgumentResultCollector(Invocation<SENDER> invocation, NavigableMap<Integer, ArgumentResult<?>> results, int lastResolvedRawArgument) {
        this.invocation = invocation;
        this.results = results;
        this.lastResolvedRawArgument = lastResolvedRawArgument;
    }

    public <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> ArgumentResultPreparedCollector<SENDER, DETERMINANT, EXPECTED, CONTEXT> prepareCollector(CONTEXT contextBox) {
        return new ArgumentResultPreparedCollector<>(this.invocation, this.results, this.lastResolvedRawArgument, contextBox);
    }

    public List<ArgumentResult<?>> getResults() {
        return Collections.unmodifiableList(new ArrayList<>(this.results.values()));
    }

    public static <SENDER> ArgumentResultCollector<SENDER> create(Invocation<SENDER> invocation) {
        return new ArgumentResultCollector<>(invocation, new TreeMap<>(), 0);
    }

}
