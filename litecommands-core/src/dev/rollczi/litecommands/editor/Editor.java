package dev.rollczi.litecommands.editor;

import dev.rollczi.litecommands.command.builder.CommandBuilder;

public interface Editor<SENDER> {

    CommandBuilder<SENDER> edit(CommandBuilder<SENDER> context);

}
