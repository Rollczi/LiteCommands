package dev.rollczi.litecommands.complete;

import dev.rollczi.litecommands.LiteTestSender;
import dev.rollczi.litecommands.annotations.Arg;
import dev.rollczi.litecommands.annotations.Execute;
import dev.rollczi.litecommands.annotations.Required;
import dev.rollczi.litecommands.annotations.Section;

@Section(route = "ac")
public class TestSuggestionCommand {

    @Execute
    public void execute() {}

    @Execute(route = "help")
    public void executeHelp() {}

    @Section(route = "manage")
    public static class ManageCommand {

        @Execute
        public void execute() {}

        @Execute(route = "move")
        @Required(1)
        public void executeMove(@Arg(0) LiteTestSender sender) {

        }

    }

}
