package dev.rollczi.litecommands.messages;

import dev.rollczi.litecommands.annotations.Execute;
import dev.rollczi.litecommands.annotations.Section;
import dev.rollczi.litecommands.annotations.UsageMessage;

@Section(route = "out")
@UsageMessage("out")
public class MessagesTestCommand {

    @Section(route = "in")
    @UsageMessage("in")
    public static class In {

        @Execute(route = "empty", required = 10)
        public void executeEmpty() {}

        @Execute(required = 10)
        @UsageMessage("execute")
        public void execute() {}

    }

}
