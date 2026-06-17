package dev.rollczi.litecommands.suggestion.event;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.event.Event;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class SuggestionResultEvent implements Event {

    private final Invocation<?> invocation;
    private final CommandExecutor<?> executor;
    private final Argument<?> argument;
    private final Parser<?, ?> parser;
    private final SuggestionResult result;

    public SuggestionResultEvent(Invocation<?> invocation, CommandExecutor<?> executor, Argument<?> argument, Parser<?, ?> parser, SuggestionResult result) {
        this.invocation = invocation;
        this.executor = executor;
        this.argument = argument;
        this.parser = parser;
        this.result = result;
    }

    public Invocation<?> getInvocation() {
        return invocation;
    }

    public CommandExecutor<?> getExecutor() {
        return executor;
    }

    public Argument<?> getArgument() {
        return argument;
    }

    public Parser<?, ?> getParser() {
        return parser;
    }

    public SuggestionResult getResult() {
        return result;
    }

}
