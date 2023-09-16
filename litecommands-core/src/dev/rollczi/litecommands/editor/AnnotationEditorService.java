package dev.rollczi.litecommands.editor;

import dev.rollczi.litecommands.command.builder.CommandBuilder;

import java.util.HashMap;
import java.util.Map;

public class AnnotationEditorService<SENDER> { //TODO implement

    private final Map<String, Editor<SENDER>> editorsByEditorName = new HashMap<>();

    public void registerEditorKey(String name, Editor<SENDER> editor) {
        this.editorsByEditorName.put(name, editor);
    }

    public CommandBuilder<SENDER> edit(Class<?> type, CommandBuilder<SENDER> context) {

        for (Edit editAnnotation : type.getAnnotationsByType(Edit.class)) {
            Editor<SENDER> commandEditor = this.editorsByEditorName.get(editAnnotation.key());

            if (commandEditor != null) {
                context = commandEditor.edit(context);
            }
        }

        return context;
    }

}
