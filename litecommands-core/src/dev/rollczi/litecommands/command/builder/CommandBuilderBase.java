package dev.rollczi.litecommands.command.builder;

import dev.rollczi.litecommands.command.CommandExecutorProvider;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class CommandBuilderBase<SENDER> extends CommandBuilderChildrenBase<SENDER> implements CommandBuilder<SENDER> {

    protected @Nullable String name;
    protected final List<String> aliases = new ArrayList<>();
    protected final List<CommandExecutorProvider<SENDER>> executors = new ArrayList<>();
    protected boolean enabled = true;
    protected Meta meta = Meta.create();

    protected String shortName;
    protected List<String> shortAliases = new ArrayList<>();

    protected CommandBuilderDummyPrefix<SENDER> dummyPrefix;

    @Override
    public @NotNull CommandBuilder<SENDER> name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public @NotNull CommandBuilder<SENDER> aliases(List<String> aliases) {
        this.aliases.clear();
        this.aliases.addAll(aliases);

        return this;
    }

    @Override
    public @NotNull CommandBuilder<SENDER> aliases(String... aliases) {
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

        return this.aliases.contains(name);
    }

    @Override
    public boolean hasSimilarNames(CommandBuilder<SENDER> context) {
        for (String name : context.names()) {
            if (this.isNameOrAlias(name)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public @NotNull CommandBuilder<SENDER> enable() {
        this.enabled = true;
        return this;
    }

    @Override
    public @NotNull CommandBuilder<SENDER> enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public @NotNull CommandBuilder<SENDER> disable() {
        this.enabled = false;
        return this;
    }

    @Override
    public @NotNull CommandBuilder<SENDER> appendExecutor(CommandExecutorProvider<SENDER> executor) {
        this.executors.add(executor);
        return this;
    }

    @Override
    public Collection<CommandExecutorProvider<SENDER>> executors() {
        return Collections.unmodifiableCollection(this.executors);
    }

    @Override
    public CommandBuilder<SENDER> applyMeta(UnaryOperator<Meta> operator) {
        this.meta = operator.apply(this.meta);
        return this;
    }

    @Override
    public Meta meta() {
        return this.meta;
    }

    @Override
    public @Nullable MetaHolder parentMeta() {
        return this.parent();
    }

    @Override
    @ApiStatus.Internal
    public CommandBuilder<SENDER> routeName(String name) {
        name = name.trim();

        if (!name.contains(" ")) {
            this.name = name;
            return this;
        }

        int separatorIndex = name.lastIndexOf(" ");
        this.name = name.substring(separatorIndex + 1);

        String namePrefix = name.substring(0, separatorIndex);

        if (this.dummyPrefix == null) {
            this.dummyPrefix = new CommandBuilderDummyPrefix<>(this);
        }

        return this.dummyPrefix.dummyName(namePrefix);
    }

    @Override
    @ApiStatus.Internal
    public CommandBuilder<SENDER> routeAliases(List<String> aliases) {
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
            this.dummyPrefix = new CommandBuilderDummyPrefix<>(this);
        }

        return this.dummyPrefix.dummyAliases(aliasesPrefix);
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
    public CommandBuilder<SENDER> applyOnRoute(UnaryOperator<CommandBuilder<SENDER>> apply) {
        return apply.apply(this);
    }

    @Override
    public CommandBuilder<SENDER> getRealRoute() {
        return this;
    }

    @Override
    @ApiStatus.Internal
    public CommandBuilder<SENDER> shortRouteName(String name) {
        this.shortName = name;
        return this;
    }

    @Override
    @ApiStatus.Internal
    public String shortRouteName() {
        return this.shortName;
    }

    @Override
    @ApiStatus.Internal
    public CommandBuilder<SENDER> shortRouteAliases(List<String> aliases) {
        this.shortAliases = aliases;
        return this;
    }

    @Override
    @ApiStatus.Internal
    public List<String> shortRouteAliases() {
        return Collections.unmodifiableList(this.shortAliases);
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
    public @Nullable CommandBuilder<SENDER> parent() {
        return dummyPrefix;
    }

    @Override
    public Collection<CommandRoute<SENDER>> build(CommandRoute<SENDER> parent) {
        Set<CommandRoute<SENDER>> routes = new LinkedHashSet<>();

        CommandRoute<SENDER> mainRoute = CommandRoute.create(parent, this.name, this.aliases);
        mainRoute.meta().apply(this.meta);
        for (CommandExecutorProvider<SENDER> executor : this.executors) {
            mainRoute.appendExecutor(executor.provide(mainRoute));
        }
        routes.add(mainRoute);

        for (CommandBuilder<SENDER> child : this.children()) {
            if (!child.buildable()) {
                continue;
            }

            if (child.shortRouteName() != null) {
                CommandRoute<SENDER> shortRoute = CommandRoute.create(parent, child.shortRouteName(), child.shortRouteAliases());
                routes.addAll(child.build(shortRoute));
            }

            for (CommandRoute<SENDER> childRoute : child.build(mainRoute)) {
                mainRoute.appendChildren(childRoute);
            }
        }

        return routes;
    }

    @Override
    public void meagre(CommandBuilder<SENDER> context) {
        this.aliases.addAll(context.aliases());

        for (CommandExecutorProvider<SENDER> executor : context.executors()) {
            this.appendExecutor(executor);
        }

        if (!context.isEnabled()) {
            this.disable();
        }

        toMeagre:
        for (CommandBuilder<SENDER> childToMeagre : context.children()) {
            for (CommandBuilder<SENDER> current : this.children.values()) {
                if (!current.hasSimilarNames(childToMeagre)) {
                    continue;
                }

                current.meagre(childToMeagre);
                continue toMeagre;
            }

            this.appendChild(childToMeagre);
        }

    }

}
