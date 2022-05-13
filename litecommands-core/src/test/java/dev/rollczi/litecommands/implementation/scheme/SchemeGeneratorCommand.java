package dev.rollczi.litecommands.implementation.scheme;


import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.block.Block;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.section.Section;

@Section(route = "lp")
class SchemeGeneratorCommand {

    @Execute
    public String set(@Arg String user, @Block("parent set") @Arg String rank) {
        return user + " -> " + rank;
    }

}
