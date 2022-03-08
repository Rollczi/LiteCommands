package dev.rollczi.litecommands.scheme;

import dev.rollczi.litecommands.annotations.Arg;
import dev.rollczi.litecommands.annotations.Execute;
import dev.rollczi.litecommands.annotations.Handler;
import dev.rollczi.litecommands.annotations.Section;
import dev.rollczi.litecommands.argument.StringArg;
import panda.std.Option;

@Section(route = "command")
public class TestSchemeUseCommand {

    @Execute(route = "hello")
    public void subcommand() {}

    @Execute(route = "hey", required = 2)
    public void otherSubcommand(@Arg(0) String arg0, @Arg(1) String arg1) {}

    @Execute(route = "siema", required = 1)
    public void otherSubcommand(@Arg(0) String arg, @Arg(1) @Handler(StringArg.class) Option<String> optionArg) {}

}


