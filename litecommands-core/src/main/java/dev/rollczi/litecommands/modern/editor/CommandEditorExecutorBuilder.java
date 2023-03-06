package dev.rollczi.litecommands.modern.editor;

import dev.rollczi.litecommands.modern.command.CommandExecutor;
import dev.rollczi.litecommands.modern.meta.CommandMeta;
import dev.rollczi.litecommands.modern.meta.CommandMetaHolder;

import java.util.function.UnaryOperator;

public class CommandEditorExecutorBuilder<SENDER> implements CommandMetaHolder {

    private CommandExecutor<SENDER> executor;
    private CommandMeta meta = CommandMeta.create();

    public CommandEditorExecutorBuilder() {
    }

    public CommandEditorExecutorBuilder(CommandExecutor<SENDER> executor) {
        this.executor = executor;
    }

    public void setExecutor(CommandExecutor<SENDER> executor) {
        this.executor = executor;
    }

    public void applyMeta(UnaryOperator<CommandMeta> operator) {
        this.meta = operator.apply(this.meta);
    }

    public boolean buildable() {
        return this.executor != null;
    }

    public CommandExecutor<SENDER> build() {
        this.executor.getMeta().apply(this.meta);

        return this.executor;
    }

    @Override
    public CommandMeta getMeta() {
        return this.meta;
    }

}
