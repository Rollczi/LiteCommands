package dev.rollczi.litecommands.modern.command.editor;

import dev.rollczi.litecommands.modern.command.CommandExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

class CommandEditorContextDummy<SENDER> extends CommandEditorContextBase<SENDER> implements CommandEditorContext<SENDER> {

    protected CommandEditorContext<SENDER> parent;

    public CommandEditorContextDummy(CommandEditorContext<SENDER> parent) {
        this.parent = parent;
    }

    void dummyName(String name) {
        super.routeName(name);
    }

    void dummyAliases(List<String> aliases) {
        super.routeAliases(aliases);
    }

    @Override
    public Collection<CommandEditorContext<SENDER>> children() {
        return Collections.singletonList(this.parent);
    }

    @Override
    public CommandEditorContext<SENDER> routeName(String name) {
        return this.parent.routeName(name);
    }

    @Override
    public CommandEditorContext<SENDER> routeAliases(List<String> aliases) {
        return this.parent.routeAliases(aliases);
    }

    @Override
    public @NotNull CommandEditorContext<SENDER> editChild(String name, UnaryOperator<CommandEditorContext<SENDER>> operator) {
        this.parent = operator.apply(this.parent);
        return this;
    }

    @Override
    public CommandEditorContext<SENDER> applyOnRoute(UnaryOperator<CommandEditorContext<SENDER>> apply) {
        this.parent = this.parent.applyOnRoute(apply);
        return this;
    }
}
