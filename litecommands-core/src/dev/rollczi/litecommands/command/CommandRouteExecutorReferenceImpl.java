package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.command.executor.CommandExecutor;

import dev.rollczi.litecommands.priority.MutablePriorityList;
import dev.rollczi.litecommands.priority.PriorityList;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.Unmodifiable;

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
    public @Unmodifiable PriorityList<CommandExecutor<SENDER>> getExecutors() {
        MutablePriorityList<CommandExecutor<SENDER>> executors = new MutablePriorityList<>();

        for (CommandExecutor<SENDER> executor : super.getExecutors()) {
            executors.add(executor);
        }

        executors.add(referenceExecutor);

        return executors;
    }

}
