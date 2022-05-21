package dev.rollczi.litecommands.factory;

import java.util.HashMap;
import java.util.Map;

public class CommandEditorRegistry {

    private final Map<Class<?>, CommandEditor> editors = new HashMap<>();
    private final Map<String, CommandEditor> editorsByName = new HashMap<>();

    public void registerEditor(Class<?> on, CommandEditor editor) {
        editors.put(on, editor);
    }

    public void registerEditor(String on, CommandEditor editor) {
        editorsByName.put(on, editor);
    }

    public CommandEditor.State apply(Class<?> sectionClass, CommandEditor.State root) {
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
