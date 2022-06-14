package dev.rollczi.litecommands.factory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CommandEditorRegistry {

    private final Map<Class<?>, CommandEditor> editors = new HashMap<>();
    private final Map<String, CommandEditor> editorsByName = new HashMap<>();
    private final Set<CommandEditor> globalEditors = new HashSet<>();

    public void registerEditor(Class<?> on, CommandEditor editor) {
        editors.put(on, editor);
    }

    public void registerEditor(String on, CommandEditor editor) {
        editorsByName.put(on, editor);
    }

    public void registerGlobalEditor(CommandEditor editor) {
        globalEditors.add(editor);
    }

    public CommandEditor.State apply(Class<?> sectionClass, CommandEditor.State root) {
        for (CommandEditor editor : globalEditors) {
            root = editor.edit(root);
        }

        CommandEditor editorByName = editorsByName.get(root.getName());

        if (editorByName != null) {
            root = editorByName.edit(root);
        }

        CommandEditor editor = editors.get(sectionClass);

        if (editor != null) {
            root = editor.edit(root);
        }

        return root;
    }

}
