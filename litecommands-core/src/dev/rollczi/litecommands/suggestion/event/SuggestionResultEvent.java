package dev.rollczi.litecommands.suggestion.event;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.event.Event;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public record SuggestionResultEvent(
    CommandExecutor<?> executor,
    Argument<?> argument,
    SuggestionResult result
) implements Event { }
