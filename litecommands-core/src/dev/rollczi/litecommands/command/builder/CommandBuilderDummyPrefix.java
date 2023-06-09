package dev.rollczi.litecommands.command.builder;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

class CommandBuilderDummyPrefix<SENDER> extends CommandBuilderBase<SENDER> implements CommandBuilder<SENDER> {

    protected CommandBuilder<SENDER> children;

    public CommandBuilderDummyPrefix(CommandBuilder<SENDER> children) {
        this.children = children;
    }

    CommandBuilder<SENDER> dummyName(String name) {
        return super.routeName(name);
    }

    CommandBuilder<SENDER> dummyAliases(List<String> aliases) {
        return super.routeAliases(aliases);
    }

    @Override
    public Collection<CommandBuilder<SENDER>> children() {
        return Collections.singletonList(this.children);
    }

    @Override
    public CommandBuilder<SENDER> routeName(String name) {
        return this.children.routeName(name);
    }

    @Override
    public CommandBuilder<SENDER> routeAliases(List<String> aliases) {
        if (aliases.isEmpty()) {
            return this;
        }

        return this.children.routeAliases(aliases);
    }

    @Override
    public @NotNull CommandBuilder<SENDER> editChild(String name, UnaryOperator<CommandBuilder<SENDER>> operator) {
        if (this.children.name().equals(name)) {
            this.children = operator.apply(this.children);
            return this;
        }

        throw new IllegalArgumentException("Child with name " + name + " not found");
    }

    @Override
    public CommandBuilder<SENDER> applyOnRoute(UnaryOperator<CommandBuilder<SENDER>> apply) {
        this.children = this.children.applyOnRoute(apply);
        return this;
    }

    @Override
    public CommandBuilder<SENDER> route() {
        return this.children.route();
    }

}
