package dev.rollczi.litecommands.command.builder;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaCollector;
import dev.rollczi.litecommands.meta.MetaHolder;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class CommandBuilderExecutor<SENDER> implements MetaHolder {

    private CommandExecutor<SENDER, ?> executor;
    private Meta meta = Meta.create();

    public CommandBuilderExecutor() {
    }

    public CommandBuilderExecutor(CommandExecutor<SENDER, ?> executor) {
        this.executor = executor;
    }

    public void setExecutor(CommandExecutor<SENDER, ?> executor) {
        this.executor = executor;
    }

    public CommandBuilderExecutor<SENDER> applyMeta(UnaryOperator<Meta> operator) {
        this.meta = operator.apply(this.meta);
        return this;
    }

    public boolean buildable() {
        return this.executor != null;
    }

    public CommandExecutor<SENDER, ?> build() {
        this.executor.meta().apply(this.meta);

        return this.executor;
    }

    @Override
    public void editMeta(Consumer<Meta> operator) {
        operator.accept(this.meta);
    }

    @Override
    public Meta meta() {
        return this.meta;
    }

    @Override
    public MetaCollector metaCollector() {
        return MetaCollector.of(meta);
    }

}
