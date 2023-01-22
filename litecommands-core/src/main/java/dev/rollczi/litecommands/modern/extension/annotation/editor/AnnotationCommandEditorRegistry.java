package dev.rollczi.litecommands.modern.extension.annotation.editor;

import dev.rollczi.litecommands.modern.command.editor.CommandEditor;
import dev.rollczi.litecommands.modern.command.editor.CommandEditorContext;
import dev.rollczi.litecommands.modern.command.editor.CommandEditorRegistry;

import java.util.HashMap;
import java.util.Map;

public class AnnotationCommandEditorRegistry extends CommandEditorRegistry {

    private final Map<Class<?>, CommandEditor> editorsByClass = new HashMap<>();
    private final Map<String, CommandEditor> editorsByEditorName = new HashMap<>();

    public void registerEditor(Class<?> clazz, CommandEditor editor) {
        this.editorsByClass.put(clazz, editor);
    }

    public void registerEditorKey(String name, CommandEditor editor) {
        this.editorsByEditorName.put(name, editor);
    }

    @Override
    public CommandEditorContext edit(CommandEditorContext context) {
        throw new UnsupportedOperationException("Use edit(Object, CommandEditorContext) instead");
    }

    public CommandEditorContext edit(Object instance, CommandEditorContext context) {
        CommandEditor editor = this.editorsByClass.get(instance.getClass());

        if (editor != null) {
            context = editor.edit(context);
        }

        for (Editor editorAnnotation : instance.getClass().getAnnotationsByType(Editor.class)) {
            CommandEditor commandEditor = editorsByEditorName.get(editorAnnotation.key());

            if (commandEditor != null) {
                context = commandEditor.edit(context);
            }
        }

        return context;
    }
}
