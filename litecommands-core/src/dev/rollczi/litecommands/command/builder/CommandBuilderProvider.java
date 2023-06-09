package dev.rollczi.litecommands.command.builder;

import java.util.List;

public interface CommandBuilderProvider<SENDER> {

    List<CommandBuilder<SENDER>> getCommands();

}
