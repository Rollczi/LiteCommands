package dev.rollczi.litecommands.modern.command.editor;

import dev.rollczi.litecommands.modern.command.CommandRoute;
import dev.rollczi.litecommands.modern.command.meta.CommandMeta;

class CommandEditorContextImpl extends CommandEditorContextBase implements CommandEditorContext {

    private String command;
    private String aliases;

    private CommandMeta meta = new CommandMeta();


    public CommandRoute buildRoute() {
        return null; //TODO
    }

}
