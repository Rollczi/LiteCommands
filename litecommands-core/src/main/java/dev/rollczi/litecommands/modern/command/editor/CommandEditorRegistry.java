package dev.rollczi.litecommands.modern.command.editor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CommandEditorRegistry {

    private final Map<String, CommandEditor> editors = new HashMap<>();
    private final Map<Class<?>, CommandEditor> editorsByClass = new HashMap<>();
    private final Set<CommandEditor> globalEditors = new HashSet<>();

}
