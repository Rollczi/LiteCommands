package dev.rollczi.litecommands.modern.editor;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CommandEditorContextRegistry<SENDER> {

    private final Set<CommandEditorContextProvider<SENDER>> providers = new LinkedHashSet<>();

    public void register(CommandEditorContextProvider<SENDER> provider) {
        this.providers.add(provider);
    }

    public List<CommandEditorContext<SENDER>> extractAndMergeContexts() {
        List<CommandEditorContext<SENDER>> collectedContexts = new ArrayList<>();

        root:
        for (CommandEditorContextProvider<SENDER> provider : this.providers) {
            CommandEditorContext<SENDER> context = provider.getContext();

            for (CommandEditorContext<SENDER> collectedContext : collectedContexts) {
                if (!collectedContext.hasSimilarNames(context)) {
                    continue;
                }

                collectedContext.meagre(context);
                continue root;
            }

            collectedContexts.add(context);
        }

        return collectedContexts;
    }

}
