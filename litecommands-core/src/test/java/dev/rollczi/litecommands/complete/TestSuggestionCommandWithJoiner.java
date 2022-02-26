package dev.rollczi.litecommands.complete;

import dev.rollczi.litecommands.annotations.Execute;
import dev.rollczi.litecommands.annotations.Joiner;
import dev.rollczi.litecommands.annotations.MinArgs;
import dev.rollczi.litecommands.annotations.Section;

@Section(route = "joiner")
public class TestSuggestionCommandWithJoiner {

    @Execute @MinArgs(1)
    public void execute(@Joiner String text) {}

}
