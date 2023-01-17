package dev.rollczi.litecommands.modern.command.editor;

import java.util.List;

public class CommandEditorContextDummy extends CommandEditorContextBase implements CommandEditorContext {

    private String command;
    private List<String> aliases;
    private final CommandEditorContext parent;

    public CommandEditorContextDummy(CommandEditorContext parent) {
        this.parent = parent;
    }

}
