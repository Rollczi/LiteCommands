package dev.rollczi.litecommands.modern.editor;

import dev.rollczi.litecommands.modern.command.CommandRoute;
import dev.rollczi.litecommands.modern.meta.CommandMeta;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

abstract class CommandEditorContextBase<SENDER> implements CommandEditorContext<SENDER> {

    protected @Nullable String name;
    protected final List<String> aliases = new ArrayList<>();
    protected final Map<String, CommandEditorContext<SENDER>> children = new HashMap<>();
    protected final List<CommandEditorExecutorBuilder<SENDER>> executors = new ArrayList<>();
    protected boolean enabled = true;
    protected CommandMeta meta = CommandMeta.create();

    protected CommandEditorContextDummyPrefix<SENDER> dummyPrefix;

    @Override
    public @NotNull CommandEditorContext<SENDER> name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public @NotNull CommandEditorContext<SENDER> aliases(List<String> aliases) {
        this.aliases.clear();
        this.aliases.addAll(aliases);

        return this;
    }

    @Override
    public @NotNull CommandEditorContext<SENDER> aliases(String... aliases) {
        this.aliases.clear();
        Collections.addAll(this.aliases, aliases);

        return this;
    }

    @Override
    public List<String> aliases() {
        return Collections.unmodifiableList(this.aliases);
    }

    @Override
    public List<String> names() {
        List<String> names = new ArrayList<>();
        names.add(this.name);
        names.addAll(this.aliases);

        return Collections.unmodifiableList(names);
    }

    @Override
    public boolean isNameOrAlias(String name) {
        if (this.name != null && this.name.equals(name)) {
            return true;
        }

        if (this.aliases.contains(name)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean hasSimilarNames(CommandEditorContext<SENDER> context) {
        for (String name : context.names()) {
            if (this.isNameOrAlias(name)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public @NotNull CommandEditorContext<SENDER> enable() {
        this.enabled = true;
        return this;
    }

    @Override
    public @NotNull CommandEditorContext<SENDER> disable() {
        this.enabled = false;
        return this;
    }

    @Override
    public @NotNull CommandEditorContext<SENDER> editChild(String name, UnaryOperator<CommandEditorContext<SENDER>> operator) {
        CommandEditorContext<SENDER> child = this.children.get(name);

        if (child == null) {
            throw new IllegalArgumentException("Child " + name + " not found");
        }

        child = operator.apply(child);
        this.children.put(name, child);
        return this;
    }

    @Override
    public @NotNull CommandEditorContext<SENDER> appendChild(String name, UnaryOperator<CommandEditorContext<SENDER>> operator) {
        CommandEditorContext<SENDER> child = new CommandEditorContextImpl<>();
        child.name(name);

        child = operator.apply(child);
        this.children.put(name, child);
        return this;
    }

    @Override
    public @NotNull CommandEditorContext<SENDER> appendChild(CommandEditorContext<SENDER> context) {
        this.children.put(context.name(), context);
        return this;
    }

    @Override
    public Collection<CommandEditorContext<SENDER>> children() {
        return Collections.unmodifiableCollection(this.children.values());
    }

    @Override
    public @NotNull CommandEditorContext<SENDER> appendExecutor(CommandEditorExecutorBuilder<SENDER> executor) {
        this.executors.add(executor);
        return this;
    }

    @Override
    public Collection<CommandEditorExecutorBuilder<SENDER>> executors() {
        return Collections.unmodifiableCollection(this.executors);
    }

    @Override
    public CommandEditorContext<SENDER> applyMeta(UnaryOperator<CommandMeta> operator) {
        this.meta = operator.apply(this.meta);
        return this;
    }

    @Override
    public CommandMeta getMeta() {
        return this.meta;
    }

    @Override
    @ApiStatus.Internal
    public CommandEditorContext<SENDER> routeName(String name) {
        name = name.trim();

        if (!name.contains(" ")) {
            this.name = name;
            return this;
        }

        int separatorIndex = name.lastIndexOf(" ");
        this.name = name.substring(separatorIndex + 1);

        String namePrefix = name.substring(0, separatorIndex);

        if (this.dummyPrefix == null) {
            this.dummyPrefix = new CommandEditorContextDummyPrefix<>(this);
        }

        this.dummyPrefix.dummyName(namePrefix);
        return this.dummyPrefix;
    }

    @Override
    @ApiStatus.Internal
    public CommandEditorContext<SENDER> routeAliases(List<String> aliases) {
        if (aliases.isEmpty()) {
            return this;
        }

        int countDummy = this.countDummy(aliases.get(0));

        for (String alias : aliases) {
            this.validName(alias);

            if (this.countDummy(alias) != countDummy) {
                throw new IllegalArgumentException("Aliases must have the same structure");
            }
        }

        if (countDummy == 0) {
            this.aliases.clear();
            this.aliases.addAll(aliases);
            return this;
        }

        List<String> aliasesPrefix = new ArrayList<>();

        for (String alias : aliases) {
            alias = alias.trim();

            int separatorIndex = alias.lastIndexOf(" ");
            String command = alias.substring(separatorIndex + 1);

            this.aliases.add(command);
            aliasesPrefix.add(alias.substring(0, separatorIndex));
        }

        if (this.dummyPrefix == null) {
            this.dummyPrefix = new CommandEditorContextDummyPrefix<>(this);
        }

        this.dummyPrefix.dummyAliases(aliasesPrefix);
        return this.dummyPrefix;
    }

    private int countDummy(String name) {
        return name.split(" ").length - 1;
    }

    private void validName(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (!name.trim().equals(name)) {
            throw new IllegalArgumentException("Name cannot start or end with space");
        }
    }

    @Override
    @ApiStatus.Internal
    public CommandEditorContext<SENDER> applyOnRoute(UnaryOperator<CommandEditorContext<SENDER>> apply) {
        return apply.apply(this);
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    @ApiStatus.Internal
    public boolean buildable() {
        return this.enabled && this.name != null && !this.name.isEmpty();
    }

    @Override
    public Collection<CommandRoute<SENDER>> build(CommandRoute<SENDER> parent) {
        CommandRoute<SENDER> route = CommandRoute.create(parent, this.name, this.aliases);

        route.getMeta().apply(this.meta);

        for (CommandEditorExecutorBuilder<SENDER> executor : this.executors) {
            if (!executor.buildable()) {
                continue;
            }

            route.appendExecutor(executor.build());
        }

        for (CommandEditorContext<SENDER> child : this.children.values()) {
            if (!child.buildable()) {
                continue;
            }

            for (CommandRoute<SENDER> childRoute : child.build(route)) {
                route.appendChildren(childRoute);
            }
        }

        return Collections.singleton(route);
    }

    @Override
    public void meagre(CommandEditorContext<SENDER> context) {
        this.aliases.addAll(context.aliases());

        for (CommandEditorExecutorBuilder<SENDER> executor : context.executors()) {
            this.appendExecutor(executor);
        }

        if (!context.isEnabled()) {
            this.disable();
        }

        toMeagre:
        for (CommandEditorContext<SENDER> childToMeagre : context.children()) {
            for (CommandEditorContext<SENDER> current : this.children.values()) {
                if (!current.hasSimilarNames(childToMeagre)) {
                    continue;
                }

                current.meagre(childToMeagre);
                continue toMeagre;
            }

            this.appendChild(childToMeagre);
        }

    }

    @Override
    public void editMeta(Consumer<CommandMeta> operator) {
        operator.accept(this.meta);
    }

}