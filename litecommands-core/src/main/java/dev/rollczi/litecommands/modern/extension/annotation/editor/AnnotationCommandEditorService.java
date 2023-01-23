package dev.rollczi.litecommands.modern.extension.annotation.editor;

import dev.rollczi.litecommands.modern.command.editor.CommandEditor;
import dev.rollczi.litecommands.modern.command.editor.CommandEditorContext;
import dev.rollczi.litecommands.modern.command.editor.CommandEditorService;

import java.util.HashMap;
import java.util.Map;

public class AnnotationCommandEditorService {

    private final CommandEditorService pattern;

    private final Map<Class<?>, CommandEditor> editorsByClass = new HashMap<>();
    private final Map<String, CommandEditor> editorsByEditorName = new HashMap<>();

    public AnnotationCommandEditorService(CommandEditorService pattern) {
        this.pattern = pattern;
    }

    public void registerEditor(Class<?> clazz, CommandEditor editor) {
        this.editorsByClass.put(clazz, editor);
    }

    public void registerEditorKey(String name, CommandEditor editor) {
        this.editorsByEditorName.put(name, editor);
    }

    public CommandEditorContext edit(Object instance, CommandEditorContext context) {
        CommandEditor editor = this.editorsByClass.get(instance.getClass());

        if (editor != null) {
            context = editor.edit(context);
        }

        for (Editor editorAnnotation : instance.getClass().getAnnotationsByType(Editor.class)) {
            CommandEditor commandEditor = this.editorsByEditorName.get(editorAnnotation.key());

            if (commandEditor != null) {
                context = commandEditor.edit(context);
            }
        }

        return this.pattern.edit(context);
    }

}
