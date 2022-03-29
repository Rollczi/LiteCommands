package dev.rollczi.litecommands.multisections;

import dev.rollczi.litecommands.annotations.Execute;
import dev.rollczi.litecommands.annotations.Section;
import dev.rollczi.litecommands.valid.ValidationCommandException;
import dev.rollczi.litecommands.valid.ValidationInfo;

@Section(route = "ac")
public class MultiSectionTestCommand {

    @Execute
    public void execute() {
        throw new ValidationCommandException("ac");
    }

    @Execute(route = "help")
    public void executeHelp() {
        throw new ValidationCommandException("help");
    }

}
