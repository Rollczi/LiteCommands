package dev.rollczi.litecommands.modern.command.editor;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.UnaryOperator;

class CommandEditorContextDummy extends CommandEditorContextBase implements CommandEditorContext {

    protected CommandEditorContext parent;

    public CommandEditorContextDummy(CommandEditorContext parent) {
        this.parent = parent;
    }

    void dummyName(String name) {
        super.routeName(name);
    }

    void dummyAliases(List<String> aliases) {
        super.routeAliases(aliases);
    }

    @Override
    public CommandEditorContext routeName(String name) {
        return this.parent.routeName(name);
    }

    @Override
    public CommandEditorContext routeAliases(List<String> aliases) {
        return this.parent.routeAliases(aliases);
    }

    @Override
    public @NotNull CommandEditorContext editChild(String name, UnaryOperator<CommandEditorContext> operator) {
        this.parent = operator.apply(this.parent);
        return this;
    }

    @Override
    public CommandEditorContext applyOnRoute(UnaryOperator<CommandEditorContext> apply) {
        this.parent = this.parent.applyOnRoute(apply);
        return this;
    }
}
