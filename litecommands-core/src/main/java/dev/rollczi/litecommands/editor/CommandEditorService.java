package dev.rollczi.litecommands.editor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CommandEditorService<SENDER> {

    private final Map<String, CommandEditor<SENDER>> editors = new HashMap<>();
    private final Set<CommandEditor<SENDER>> globalEditors = new HashSet<>();

    public void registerEditor(String name, CommandEditor<SENDER> editor) {
        this.editors.put(name, editor);
    }

    public void registerGlobalEditor(CommandEditor<SENDER> editor) {
        this.globalEditors.add(editor);
    }

    public CommandEditorContext<SENDER> edit(CommandEditorContext<SENDER> context) {
        for (CommandEditor<SENDER> editor : this.globalEditors) {
            context = editor.edit(context);
        }

        for (String name : context.names()) {
            CommandEditor<SENDER> editor = this.editors.get(name);

            if (editor != null) {
                context = editor.edit(context);
            }
        }

        return context;
    }

}
