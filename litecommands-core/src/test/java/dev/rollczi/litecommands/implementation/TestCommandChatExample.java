package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.argument.flag.Flag;
import dev.rollczi.litecommands.argument.joiner.Joiner;
import dev.rollczi.litecommands.argument.option.Opt;
import dev.rollczi.litecommands.command.section.Section;
import panda.std.Option;

@Section(route = "ac", aliases = "adminchat")
class TestCommandChatExample {

    @Execute
    public String unset(@Flag("-s") boolean silent, @Joiner String text) {
        return silent + " -> " + text;
    }

    @Execute(route = "key")
    public String unset(@Opt(String.class) Option<String> first) {
        return first.isPresent() ? first.get() : "null";
    }

}
