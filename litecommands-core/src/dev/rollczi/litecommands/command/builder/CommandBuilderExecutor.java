package dev.rollczi.litecommands.command.builder;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.command.executor.CommandExecutorFactory;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import org.jetbrains.annotations.Nullable;

import java.util.function.UnaryOperator;

public class CommandBuilderExecutor<SENDER> implements MetaHolder {

    private final MetaHolder parent;
    private CommandExecutorFactory<SENDER> executorFactory;
    private Meta meta = Meta.create();

    public CommandBuilderExecutor(MetaHolder parent) {
        this.parent = parent;
    }

    public CommandBuilderExecutor(MetaHolder parent, CommandExecutorFactory<SENDER> executorFactory) {
        this.parent = parent;
        this.executorFactory = executorFactory;
    }

    public void setExecutorFactory(CommandExecutorFactory<SENDER> factory) {
        this.executorFactory = factory;
    }

    public CommandBuilderExecutor<SENDER> applyMeta(UnaryOperator<Meta> operator) {
        this.meta = operator.apply(this.meta);
        return this;
    }

    public boolean buildable() {
        return this.executorFactory != null;
    }

    public CommandExecutor<SENDER, ?> build(CommandRoute<SENDER> parent) {
        CommandExecutor<SENDER, ?> executor = this.executorFactory.create(parent);
        executor.meta().apply(this.meta);

        return executor;
    }

    @Override
    public Meta meta() {
        return this.meta;
    }

    @Override
    public @Nullable MetaHolder parentMeta() {
        return null;
    }

}
