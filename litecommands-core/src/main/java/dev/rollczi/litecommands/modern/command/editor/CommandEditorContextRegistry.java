package dev.rollczi.litecommands.modern.command.editor;

import java.util.LinkedHashSet;
import java.util.Set;

public class CommandEditorContextRegistry {

    private final Set<CommandEditorContextProvider> providers = new LinkedHashSet<>();

    public void register(CommandEditorContextProvider provider) {
        this.providers.add(provider);
    }

}
