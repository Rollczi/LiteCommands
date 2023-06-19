package dev.rollczi.litecommands.command.builder;

import dev.rollczi.litecommands.command.CommandExecutor;
import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.meta.CommandMetaHolder;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class CommandBuilderExecutor<SENDER> implements CommandMetaHolder {

    private CommandExecutor<SENDER, ?> executor;
    private CommandMeta meta = CommandMeta.create();

    public CommandBuilderExecutor() {
    }

    public CommandBuilderExecutor(CommandExecutor<SENDER, ?> executor) {
        this.executor = executor;
    }

    public void setExecutor(CommandExecutor<SENDER, ?> executor) {
        this.executor = executor;
    }

    public CommandBuilderExecutor<SENDER> applyMeta(UnaryOperator<CommandMeta> operator) {
        this.meta = operator.apply(this.meta);
        return this;
    }

    public boolean buildable() {
        return this.executor != null;
    }

    public CommandExecutor<SENDER, ?> build() {
        this.executor.getMeta().apply(this.meta);

        return this.executor;
    }

    @Override
    public void editMeta(Consumer<CommandMeta> operator) {
        operator.accept(this.meta);
    }

}
