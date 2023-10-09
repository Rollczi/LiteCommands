package dev.rollczi.litecommands.editor;

import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.scope.Scope;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EditorService<SENDER> {

    private final Map<Scope, Editor<SENDER>> editorsByScope = new HashMap<>();
    private final Set<Editor<SENDER>> globalEditors = new HashSet<>();

    public CommandBuilder<SENDER> edit(CommandBuilder<SENDER> context) {
        for (Editor<SENDER> editor : this.globalEditors) {
            context = editor.edit(context);
        }

        for (Map.Entry<Scope, Editor<SENDER>> entry : editorsByScope.entrySet()) {
            Scope scope = entry.getKey();
            Editor<SENDER> editor = entry.getValue();

            if (scope.isApplicable(context)) {
                context = editor.edit(context);
            }
        }

        return context;
    }


    public void editorGlobal(Editor<SENDER> editor) {
        this.globalEditors.add(editor);
    }

    public void editor(Scope scope, Editor<SENDER> editor) {
        this.editorsByScope.put(scope, editor);
    }


}
