package dev.rollczi.litecommands.annotations.editor;

import dev.rollczi.litecommands.editor.Editor;
import dev.rollczi.litecommands.command.builder.CommandBuilder;

import java.util.HashMap;
import java.util.Map;

public class AnnotationEditorService<SENDER> {

    private final Map<Class<?>, Editor<SENDER>> editorsByClass = new HashMap<>();
    private final Map<String, Editor<SENDER>> editorsByEditorName = new HashMap<>();

    public void registerEditor(Class<?> clazz, Editor<SENDER> editor) {
        this.editorsByClass.put(clazz, editor);
    }

    public void registerEditorKey(String name, Editor<SENDER> editor) {
        this.editorsByEditorName.put(name, editor);
    }

    public CommandBuilder<SENDER> edit(Class<?> type, CommandBuilder<SENDER> context) {
        Editor<SENDER> editor = this.editorsByClass.get(type);

        if (editor != null) {
            context = editor.edit(context);
        }

        for (Edit editAnnotation : type.getAnnotationsByType(Edit.class)) {
            Editor<SENDER> commandEditor = this.editorsByEditorName.get(editAnnotation.key());

            if (commandEditor != null) {
                context = commandEditor.edit(context);
            }
        }

        return context;
    }

}
