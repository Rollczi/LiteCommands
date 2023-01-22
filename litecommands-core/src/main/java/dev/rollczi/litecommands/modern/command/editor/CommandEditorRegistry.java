package dev.rollczi.litecommands.modern.command.editor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CommandEditorRegistry {

    private final Map<String, CommandEditor> editors = new HashMap<>();
    private final Set<CommandEditor> globalEditors = new HashSet<>();

    public void registerEditor(String name, CommandEditor editor) {
        this.editors.put(name, editor);
    }

    public void registerGlobalEditor(CommandEditor editor) {
        this.globalEditors.add(editor);
    }

    public CommandEditorContext edit(CommandEditorContext context) {
        for (CommandEditor editor : globalEditors) {
            context = editor.edit(context);
        }

        for (String name : context.names()) {
            CommandEditor editor = editors.get(name);

            if (editor != null) {
                context = editor.edit(context);
            }
        }

        return context;
    }

}
