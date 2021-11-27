package dev.rollczi.litecommands.messages;

import dev.rollczi.litecommands.annotations.Execute;
import dev.rollczi.litecommands.annotations.Section;
import dev.rollczi.litecommands.annotations.UsageMessage;
import dev.rollczi.litecommands.valid.ValidationCommandException;
import dev.rollczi.litecommands.valid.ValidationInfo;

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
