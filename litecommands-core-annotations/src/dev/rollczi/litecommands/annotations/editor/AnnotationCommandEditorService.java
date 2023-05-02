package dev.rollczi.litecommands.annotations.editor;

import dev.rollczi.litecommands.editor.CommandEditor;
import dev.rollczi.litecommands.editor.CommandEditorContext;

import java.util.HashMap;
import java.util.Map;

public class AnnotationCommandEditorService<SENDER> {

    private final Map<Class<?>, CommandEditor<SENDER>> editorsByClass = new HashMap<>();
    private final Map<String, CommandEditor<SENDER>> editorsByEditorName = new HashMap<>();

    public void registerEditor(Class<?> clazz, CommandEditor<SENDER> editor) {
        this.editorsByClass.put(clazz, editor);
    }

    public void registerEditorKey(String name, CommandEditor<SENDER> editor) {
        this.editorsByEditorName.put(name, editor);
    }

    public CommandEditorContext<SENDER> edit(Class<?> type, CommandEditorContext<SENDER> context) {
        CommandEditor<SENDER> editor = this.editorsByClass.get(type);

        if (editor != null) {
            context = editor.edit(context);
        }

        for (Editor editorAnnotation : type.getAnnotationsByType(Editor.class)) {
            CommandEditor<SENDER> commandEditor = this.editorsByEditorName.get(editorAnnotation.key());

            if (commandEditor != null) {
                context = commandEditor.edit(context);
            }
        }

        return context;
    }

}
