package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.argument.block.Block;
import dev.rollczi.litecommands.command.section.Section;
import panda.std.Blank;

@Section(route = "lp user", aliases = "luckperms user")
class TestCommandLuckPermsExample {

    @Execute
    public String set(@Arg String user, @Block("parent set") @Arg String rank) {
        return user + " -> " + rank;
    }

    @Execute
    public String unset(@Arg String user, @Block("parent unset") @Arg String rank) {
        return user + " -x " + rank;
    }

    @Execute(required = 2)
    public String reload(@Arg String user, @Block("reload") Blank unused) {
        return user + " -reload";
    }

}
