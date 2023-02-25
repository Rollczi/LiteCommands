package dev.rollczi.litecommands.modern.command.editor;

import dev.rollczi.litecommands.modern.command.CommandRoute;
import dev.rollczi.litecommands.modern.command.meta.CommandMeta;

class CommandEditorContextImpl<SENDER> extends CommandEditorContextBase<SENDER> implements CommandEditorContext<SENDER> {

    private CommandMeta meta = new CommandMeta();


    public CommandRoute buildRoute() {
        return null; //TODO
    }

}
