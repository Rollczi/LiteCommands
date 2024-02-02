package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.command.executor.CommandExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class CommandRouteExecutorReferenceImpl<SENDER> extends CommandRouteImpl<SENDER> {

    private final CommandExecutor<SENDER> referenceExecutor;

    CommandRouteExecutorReferenceImpl(String name, List<String> aliases, CommandRoute<SENDER> parent, CommandExecutor<SENDER> referenceExecutor) {
        super(name, aliases, parent);
        this.referenceExecutor = referenceExecutor;
    }

    CommandRouteExecutorReferenceImpl(String name, CommandRoute<SENDER> parent, CommandExecutor<SENDER> referenceExecutor) {
        this(name, Collections.emptyList(), parent, referenceExecutor);
    }

    @Override
    public boolean isReference() {
        return true;
    }

    @Override
    public List<CommandExecutor<SENDER>> getExecutors() {
        List<CommandExecutor<SENDER>> executors = new ArrayList<>(super.getExecutors());
        executors.add(referenceExecutor);

        return Collections.unmodifiableList(executors);
    }

}
