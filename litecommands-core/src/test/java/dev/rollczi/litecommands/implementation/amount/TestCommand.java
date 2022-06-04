package dev.rollczi.litecommands.implementation.amount;

import dev.rollczi.litecommands.argument.joiner.Joiner;
import dev.rollczi.litecommands.command.amount.Min;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.section.Section;

@Section(route = "test")
class TestCommand {

    @Execute
    @Min(1)
    public String execute(@Joiner String text) {
        return text;
    }

    @Execute(route = "key", min = 2)
    public String withRoute(@Joiner String text) {
        return text;
    }

}
