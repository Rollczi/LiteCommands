package dev.rollczi.litecommands.modern.editor;

import dev.rollczi.litecommands.modern.command.CommandRoute;
import dev.rollczi.litecommands.modern.meta.CommandMeta;
import dev.rollczi.litecommands.modern.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

class CommandEditorContextRootImpl<SENDER> implements CommandEditorContext<SENDER> {

    private final Map<String, CommandEditorContext<SENDER>> children = new HashMap<>();
    private final CommandMeta meta = CommandMeta.create();
    private final Map<String, CommandMeta> childrenMeta = new HashMap<>();

    @Override
    public @NotNull CommandEditorContext<SENDER> name(String name) {
        throw new UnsupportedOperationException("Cannot set name for root command");
    }

    @Override
    public String name() {
        return StringUtils.EMPTY;
    }

    @Override
    public @NotNull CommandEditorContext<SENDER> aliases(List<String> aliases) {
        throw new UnsupportedOperationException("Cannot set aliases for root command");
    }

    @Override
    public @NotNull CommandEditorContext<SENDER> aliases(String... aliases) {
        throw new UnsupportedOperationException("Cannot set aliases for root command");
    }

    @Override
    public boolean isNameOrAlias(String name) {
        return name.isEmpty();
    }

    @Override
    public boolean hasSimilarNames(CommandEditorContext<SENDER> context) {
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
    public @NotNull CommandEditorContext<SENDER> enable() {
        throw new UnsupportedOperationException("Cannot enable root command");
    }

    @Override
    public @NotNull CommandEditorContext<SENDER> disable() {
        throw new UnsupportedOperationException("Cannot disable root command");
    }

    @Override
    public @NotNull CommandEditorContext<SENDER> editChild(String name, UnaryOperator<CommandEditorContext<SENDER>> operator) {
        for (CommandEditorContext<SENDER> child : children.values()) {
            if (child.isNameOrAlias(name)) {
                CommandEditorContext<SENDER> newChild = operator.apply(child);
                children.put(child.name(), newChild);
                return this;
            }
        }

        throw new IllegalArgumentException("Cannot find child with name " + name);
    }

    @Override
    public @NotNull CommandEditorContext<SENDER> appendChild(String name, UnaryOperator<CommandEditorContext<SENDER>> operator) {
        CommandEditorContext<SENDER> child = new CommandEditorContextImpl<>();

        child = operator.apply(child);
        this.children.put(name, child);
        return this;    }

    @Override
    public @NotNull CommandEditorContext<SENDER> appendChild(CommandEditorContext<SENDER> context) {
        this.children.put(context.name(), context);
        return this;
    }

    @Override
    public Collection<CommandEditorContext<SENDER>> children() {
        return Collections.unmodifiableCollection(children.values());
    }

    @Override
    public @NotNull CommandEditorContext<SENDER> appendExecutor(CommandEditorExecutorBuilder<SENDER> executor) {
        throw new UnsupportedOperationException("Cannot append executor to root command");
    }

    @Override
    public List<CommandEditorExecutorBuilder<SENDER>> executors() {
        throw new UnsupportedOperationException("Cannot get executors from root command");
    }

    @Override
    public CommandEditorContext<SENDER> applyMeta(UnaryOperator<CommandMeta> operator) {
        throw new UnsupportedOperationException("Cannot apply meta to root command");
    }

    @Override
    public CommandMeta getMeta() {
        return meta;
    }

    @Override
    public CommandEditorContext<SENDER> routeName(String name) {
        throw new UnsupportedOperationException("Cannot set name for root command");
    }

    @Override
    public CommandEditorContext<SENDER> routeAliases(List<String> aliases) {
        throw new UnsupportedOperationException("Cannot set aliases for root command");
    }

    @Override
    public CommandEditorContext<SENDER> applyOnRoute(UnaryOperator<CommandEditorContext<SENDER>> apply) {
        throw new UnsupportedOperationException("Cannot apply on route for root command");
    }

    @Override
    public void meagre(CommandEditorContext<SENDER> context) {
        for (CommandEditorContext<SENDER> child : context.children()) {
            if (children.containsKey(child.name())) {
                children.get(child.name()).meagre(child);

                if (childrenMeta.containsKey(child.name())) {
                    childrenMeta.get(child.name()).apply(context.getMeta());
                } else {
                    childrenMeta.put(child.name(), context.getMeta().copy().apply(this.meta));
                }

            } else {
                children.put(child.name(), child);
                childrenMeta.put(child.name(), context.getMeta().copy());
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
    public Collection<CommandRoute<SENDER>> build(CommandRoute<SENDER> parent) {
        return this.children.values().stream()
            .map(senderCommandEditorContext -> senderCommandEditorContext.build(parent))
            .flatMap(Collection::stream)
            .peek(route -> route.getMeta().apply(this.childrenMeta.getOrDefault(route.getName(), this.meta)))
            .collect(Collectors.toList());
    }

    @Override
    public void editMeta(Consumer<CommandMeta> operator) {
        operator.accept(meta);
    }

}
