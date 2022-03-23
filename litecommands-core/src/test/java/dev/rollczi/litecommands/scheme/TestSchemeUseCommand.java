package dev.rollczi.litecommands.scheme;

import dev.rollczi.litecommands.annotations.*;
import dev.rollczi.litecommands.argument.StringArg;
import panda.std.Option;

@Section(route = "command")
public class TestSchemeUseCommand {

    @Execute(route = "hello")
    public void subcommand() {
    }

    @Execute(route = "hey", required = 2)
    public void otherSubcommand(@Arg(0) String arg0, @Arg(1) String arg1) {
    }

    @Execute(route = "siema", required = 1)
    public void otherSubcommand(@Arg(0) String arg, @Arg(1) @Handler(StringArg.class) Option<String> optionArg) {
    }

    @Execute(route = "custom", required = 1)
    public void customName(@Arg(0) @Name("custom") String arg) {
    }

    @Execute(route = "custominarg", required = 1)
    public void customInArg(@Arg(value = 0, name = "siema") String arg) {
    }

}


