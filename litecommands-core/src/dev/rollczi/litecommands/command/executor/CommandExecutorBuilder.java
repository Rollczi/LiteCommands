package dev.rollczi.litecommands.command.executor;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.requirement.BindRequirement;
import dev.rollczi.litecommands.requirement.ContextRequirement;
import dev.rollczi.litecommands.requirement.RequirementsResult;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CommandExecutorBuilder<SENDER> {

    private final CommandRoute<SENDER> parent;
    private final List<Argument<?>> arguments = new ArrayList<>();
    private final List<ContextRequirement<?>> contextRequirements = new ArrayList<>();
    private final List<BindRequirement<?>> bindRequirements = new ArrayList<>();
    private Consumer<LiteContext<SENDER>> executor;

    public CommandExecutorBuilder(CommandRoute<SENDER> parent) {
        this.parent = parent;
    }

    public CommandExecutorBuilder<SENDER> argument(Argument<?> argument) {
        this.arguments.add(argument);
        return this;
    }

    public boolean canBuild() {
        return executor != null;
    }

    public CommandExecutorBuilder<SENDER> arguments(Iterable<Argument<?>> arguments) {
        arguments.forEach(this::argument);
        return this;
    }

    public CommandExecutorBuilder<SENDER> contextRequirement(ContextRequirement<?> contextRequirement) {
        this.contextRequirements.add(contextRequirement);
        return this;
    }

    public CommandExecutorBuilder<SENDER> contextRequirements(Iterable<ContextRequirement<?>> contextRequirements) {
        contextRequirements.forEach(this::contextRequirement);
        return this;
    }

    public CommandExecutorBuilder<SENDER> bindRequirements(List<BindRequirement<?>> bindRequirements) {
        this.bindRequirements.addAll(bindRequirements);
        return this;
    }

    public CommandExecutorBuilder<SENDER> executor(Consumer<LiteContext<SENDER>> executor) {
        this.executor = executor;
        return this;
    }

    public CommandExecutor<SENDER> build() {
        return new SimpleCommandExecutor<>(parent, executor, arguments, contextRequirements, bindRequirements);
    }

    private static class SimpleCommandExecutor<SENDER> extends AbstractCommandExecutor<SENDER> implements CommandExecutor<SENDER> {

        private final List<Argument<?>> arguments;
        private final List<ContextRequirement<?>> contextRequirements;
        private final Consumer<LiteContext<SENDER>> executor;

        private SimpleCommandExecutor(CommandRoute<SENDER> parent, Consumer<LiteContext<SENDER>> executor, List<Argument<?>> arguments, List<ContextRequirement<?>> contextRequirements, List<BindRequirement<?>> bindRequirements) {
            super(parent, arguments, contextRequirements, bindRequirements);
            this.arguments = arguments;
            this.contextRequirements = contextRequirements;
            this.executor = executor;
        }

        @Override
        public CommandExecutorMatchResult match(RequirementsResult<SENDER> result) {
            for (Argument<?> requirement : arguments) {
                if (!result.has(requirement.getName())) {
                    return CommandExecutorMatchResult.failed(new IllegalStateException("Missing requirement " + requirement.getName()));
                }
            }

            for (ContextRequirement<?> requirement : contextRequirements) {
                if (!result.has(requirement.getName())) {
                    return CommandExecutorMatchResult.failed(new IllegalStateException("Missing requirement " + requirement.getName()));
                }
            }

            return CommandExecutorMatchResult.success(() -> {
                try {
                    LiteContext<SENDER> context = new LiteContext<>(result);
                    executor.accept(context);

                    return CommandExecuteResult.success(this, context.getReturnResult());
                }
                catch (Exception exception) {
                    return CommandExecuteResult.failed(this, exception);
                }
            });
        }

    }

}
