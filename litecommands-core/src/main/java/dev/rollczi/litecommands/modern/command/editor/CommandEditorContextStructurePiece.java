package dev.rollczi.litecommands.modern.command.editor;

import dev.rollczi.litecommands.modern.command.CommandRoute;
import dev.rollczi.litecommands.modern.command.meta.CommandMeta;

import java.util.HashMap;
import java.util.Map;

public class CommandEditorContextStructurePiece extends CommandEditorContextBase implements CommandEditorContext {

    private String command;
    private String aliases;

    private CommandMeta meta = new CommandMeta();

    private Map<String, CommandEditorContextStructurePiece> children = new HashMap<>();


    public CommandRoute buildRoute() {

    }

}
