package dev.rollczi.litecommands.command.builder;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CommandBuilderCollector<SENDER> {

    private final Set<CommandBuilderProvider<SENDER>> providers = new LinkedHashSet<>();

    public void add(CommandBuilderProvider<SENDER> provider) {
        this.providers.add(provider);
    }

    public List<CommandBuilder<SENDER>> collectCommands() {
        List<CommandBuilder<SENDER>> collectedBuilders = new ArrayList<>();

        for (CommandBuilderProvider<SENDER> provider : this.providers) {
            collectedBuilders.addAll(provider.getCommands());
        }

        return collectedBuilders;
    }

}
