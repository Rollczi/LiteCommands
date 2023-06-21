package dev.rollczi.litecommands.command.builder;

import dev.rollczi.litecommands.meta.MetaCollector;
import dev.rollczi.litecommands.util.StringUtil;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.meta.Meta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

class CommandBuilderRootImpl<SENDER> implements CommandBuilder<SENDER> {

    private final Map<String, CommandBuilder<SENDER>> children = new HashMap<>();
    private final Meta meta = Meta.create();
    private final Map<String, Meta> childrenMeta = new HashMap<>();

    @Override
    public @NotNull CommandBuilder<SENDER> name(String name) {
        throw new UnsupportedOperationException("Cannot set name for root command");
    }

    @Override
    public String name() {
        return StringUtil.EMPTY;
    }

    @Override
    public @NotNull CommandBuilder<SENDER> aliases(List<String> aliases) {
        throw new UnsupportedOperationException("Cannot set aliases for root command");
    }

    @Override
    public @NotNull CommandBuilder<SENDER> aliases(String... aliases) {
        throw new UnsupportedOperationException("Cannot set aliases for root command");
    }

    @Override
    public boolean isNameOrAlias(String name) {
        return name.isEmpty();
    }

    @Override
    public boolean hasSimilarNames(CommandBuilder<SENDER> context) {
        return context.name().isEmpty();
    }

    @Override
    public List<String> aliases() {
        return Collections.emptyList();
    }

    @Override
    public List<String> names() {
        return Collections.singletonList(this.name());
    }

    @Override
    public @NotNull CommandBuilder<SENDER> enable() {
        throw new UnsupportedOperationException("Cannot enable root command");
    }

    @Override
    public @NotNull CommandBuilder<SENDER> disable() {
        throw new UnsupportedOperationException("Cannot disable root command");
    }

    @Override
    public @NotNull CommandBuilder<SENDER> editChild(String name, UnaryOperator<CommandBuilder<SENDER>> operator) {
        for (CommandBuilder<SENDER> child : children.values()) {
            if (child.isNameOrAlias(name)) {
                CommandBuilder<SENDER> newChild = operator.apply(child);
                children.put(child.name(), newChild);
                return this;
            }
        }

        throw new IllegalArgumentException("Cannot find child with name " + name);
    }

    @Override
    public @NotNull CommandBuilder<SENDER> appendChild(String name, UnaryOperator<CommandBuilder<SENDER>> operator) {
        CommandBuilder<SENDER> child = new CommandBuilderImpl<>();

        child = operator.apply(child);
        this.children.put(name, child);
        return this;
    }

    @Override
    public @NotNull CommandBuilder<SENDER> appendChild(CommandBuilder<SENDER> context) {
        this.children.put(context.name(), context);
        return this;
    }

    @Override
    public Collection<CommandBuilder<SENDER>> children() {
        return Collections.unmodifiableCollection(children.values());
    }

    @Override
    public Optional<CommandBuilder<SENDER>> getChild(String test) {
        CommandBuilder<SENDER> context = children.get(test);

        if (context != null) {
            return Optional.of(context);
        }

        for (CommandBuilder<SENDER> child : children.values()) {
            if (child.isNameOrAlias(test)) {
                return Optional.of(child);
            }
        }

        return Optional.empty();
    }

    @Override
    public @NotNull CommandBuilder<SENDER> appendExecutor(CommandBuilderExecutor<SENDER> executor) {
        throw new UnsupportedOperationException("Cannot append executor to root command");
    }

    @Override
    public List<CommandBuilderExecutor<SENDER>> executors() {
        throw new UnsupportedOperationException("Cannot get executors from root command");
    }

    @Override
    public CommandBuilder<SENDER> applyMeta(UnaryOperator<Meta> operator) {
        throw new UnsupportedOperationException("Cannot apply meta to root command");
    }

    @Override
    public Meta meta() {
        return meta;
    }

    @Override
    public MetaCollector metaCollector() {
        return new CommandBuilderMetaCollector(this);
    }

    @Override
    public CommandBuilder<SENDER> routeName(String name) {
        throw new UnsupportedOperationException("Cannot set name for root command");
    }

    @Override
    public CommandBuilder<SENDER> routeAliases(List<String> aliases) {
        throw new UnsupportedOperationException("Cannot set aliases for root command");
    }

    @Override
    public CommandBuilder<SENDER> applyOnRoute(UnaryOperator<CommandBuilder<SENDER>> apply) {
        throw new UnsupportedOperationException("Cannot apply on route for root command");
    }

    @Override
    public CommandBuilder<SENDER> route() {
        return this;
    }

    @Override
    public void meagre(CommandBuilder<SENDER> context) {
        for (CommandBuilder<SENDER> child : context.children()) {
            if (children.containsKey(child.name())) {
                children.get(child.name()).meagre(child);

                if (childrenMeta.containsKey(child.name())) {
                    childrenMeta.get(child.name()).apply(context.meta());
                }
                else {
                    childrenMeta.put(child.name(), context.meta().copy().apply(this.meta));
                }

            }
            else {
                children.put(child.name(), child);
                childrenMeta.put(child.name(), context.meta().copy());
            }


        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean buildable() {
        return true;
    }

    @Override
    public @Nullable CommandBuilder<SENDER> parent() {
        return null;
    }

    @Override
    public Collection<CommandRoute<SENDER>> build(CommandRoute<SENDER> parent) {
        return this.children.values().stream()
            .map(senderCommandEditorContext -> senderCommandEditorContext.build(parent))
            .flatMap(Collection::stream)
            .peek(route -> route.meta().apply(this.childrenMeta.getOrDefault(route.getName(), this.meta)))
            .collect(Collectors.toList());
    }

    @Override
    public void editMeta(Consumer<Meta> operator) {
        operator.accept(meta);
    }

}
