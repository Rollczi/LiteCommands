package dev.rollczi.litecommands.modern.annotation.editor;

import dev.rollczi.litecommands.modern.editor.CommandEditor;
import dev.rollczi.litecommands.modern.editor.CommandEditorContext;
import dev.rollczi.litecommands.modern.editor.CommandEditorService;

import java.util.HashMap;
import java.util.Map;

public class AnnotationCommandEditorService<SENDER> {

    private final CommandEditorService<SENDER> pattern;

    private final Map<Class<?>, CommandEditor<SENDER>> editorsByClass = new HashMap<>();
    private final Map<String, CommandEditor<SENDER>> editorsByEditorName = new HashMap<>();

    public AnnotationCommandEditorService(CommandEditorService<SENDER> pattern) {
        this.pattern = pattern;
    }

    public void registerEditor(Class<?> clazz, CommandEditor<SENDER> editor) {
        this.editorsByClass.put(clazz, editor);
    }

    public void registerEditorKey(String name, CommandEditor<SENDER> editor) {
        this.editorsByEditorName.put(name, editor);
    }

    public CommandEditorContext<SENDER> edit(Object instance, CommandEditorContext<SENDER> context) {
        CommandEditor<SENDER> editor = this.editorsByClass.get(instance.getClass());

        if (editor != null) {
            context = editor.edit(context);
        }

        for (Editor editorAnnotation : instance.getClass().getAnnotationsByType(Editor.class)) {
            CommandEditor<SENDER> commandEditor = this.editorsByEditorName.get(editorAnnotation.key());

            if (commandEditor != null) {
                context = commandEditor.edit(context);
            }
        }

        return this.pattern.edit(context);
    }

}
