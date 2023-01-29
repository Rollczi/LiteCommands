package dev.rollczi.litecommands.modern.command.editor;

import dev.rollczi.litecommands.modern.command.CommandExecutor;
import dev.rollczi.litecommands.modern.command.CommandExecutorKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

abstract class CommandEditorContextBase implements CommandEditorContext {

    protected String name;
    protected final List<String> aliases = new ArrayList<>();
    protected final Map<String, CommandEditorContext> children = new HashMap<>();
    protected final Map<CommandExecutorKey, CommandExecutor> executors = new HashMap<>();
    protected boolean enabled = true;

    protected CommandEditorContextDummy dummyPrefix;

    @Override
    public @NotNull CommandEditorContext name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public @NotNull CommandEditorContext aliases(List<String> aliases) {
        this.aliases.clear();
        this.aliases.addAll(aliases);

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
    public @NotNull CommandEditorContext enable() {
        this.enabled = true;
        return this;
    }

    @Override
    public @NotNull CommandEditorContext disable() {
        this.enabled = false;
        return this;
    }

    @Override
    public @NotNull CommandEditorContext editChild(String name, UnaryOperator<CommandEditorContext> operator) {
        CommandEditorContext child = this.children.get(name);

        if (child == null) {
            throw new IllegalArgumentException("Child " + name + " not found");
        }

        child = operator.apply(child);
        this.children.put(name, child);
        return this;
    }

    @Override
    public @NotNull CommandEditorContext appendChild(String name, UnaryOperator<CommandEditorContext> operator) {
        CommandEditorContext child = new CommandEditorContextImpl();

        child = operator.apply(child);
        this.children.put(name, child);
        return this;
    }

    @Override
    public @NotNull CommandEditorContext appendChild(CommandEditorContext context) {
        this.children.put(context.name(), context);
        return this;
    }

    @Override
    public @NotNull CommandEditorContext appendExecutor(CommandExecutor executor) {
        this.executors.put(executor.getKey(), executor);
        return this;
    }

    @Override
    @ApiStatus.Internal
    public CommandEditorContext routeName(String name) {
        name = name.trim();

        if (!name.contains(" ")) {
            this.name = name;
            return this;
        }

        int separatorIndex = name.lastIndexOf(" ");
        this.name = name.substring(separatorIndex + 1);

        String namePrefix = name.substring(0, separatorIndex);

        if (this.dummyPrefix == null) {
            this.dummyPrefix = new CommandEditorContextDummy(this);
        }

        this.dummyPrefix.dummyName(namePrefix);
        return this.dummyPrefix;
    }

    @Override
    @ApiStatus.Internal
    public CommandEditorContext routeAliases(List<String> aliases) {
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
            this.dummyPrefix = new CommandEditorContextDummy(this);
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
    public CommandEditorContext applyOnRoute(UnaryOperator<CommandEditorContext> apply) {
        return apply.apply(this);
    }

    @Override
    public boolean buildable() {
        return false;
    }
}
