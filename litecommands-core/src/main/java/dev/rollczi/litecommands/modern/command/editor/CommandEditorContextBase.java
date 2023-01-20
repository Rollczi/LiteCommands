package dev.rollczi.litecommands.modern.command.editor;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

abstract class CommandEditorContextBase implements CommandEditorContext {

    protected String name;
    protected final List<String> aliases = new ArrayList<>();
    protected final Map<String, CommandEditorContext> children = new HashMap<>();
    protected boolean enabled = true;

    protected CommandEditorContextDummy dummyPrefix;

    @Override
    public @NotNull CommandEditorContext name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public @NotNull CommandEditorContext aliases(List<String> aliases) {
        this.aliases.clear();;
        this.aliases.addAll(aliases);

        return this;
    }

    @Override
    public List<String> aliases() {
        return Collections.unmodifiableList(aliases);
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
        CommandEditorContext child = children.get(name);
        operator.apply(child);
        children.put(name, child);
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

        int countDummy = countDummy(aliases.get(0));

        for (String alias : aliases) {
            validName(alias);

            if (countDummy(alias) != countDummy) {
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
    public CommandEditorContext applyOnRoute(Function<CommandEditorContext, CommandEditorContext> apply) {
        return apply.apply(this);
    }

    @Override
    public boolean buildable() {
        return false;
    }
}
