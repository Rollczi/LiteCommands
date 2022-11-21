package dev.rollczi.litecommands.factory;

import dev.rollczi.litecommands.command.count.CountValidator;
import dev.rollczi.litecommands.meta.CommandMeta;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class CommandState implements CommandEditor.State {

    private final String name;
    private final List<Route> routesBefore = new ArrayList<>();
    private final Set<String> aliases = new HashSet<>();
    private final Set<CommandState> children = new HashSet<>();
    private final Map<Method, CommandState> executors = new HashMap<>();
    private final CommandMeta meta;
    private final boolean canceled;

    public CommandState() {
        this.name = null;
        this.meta = CommandMeta.create();
        this.canceled = false;
    }

    private CommandState(String name, List<Route> routesBefore, Set<String> aliases, Set<CommandState> children, Map<Method, CommandState> executors, CommandMeta meta, boolean canceled) {
        this.name = name;
        this.meta = meta;
        this.canceled = canceled;
        this.routesBefore.addAll(routesBefore);
        this.aliases.addAll(aliases);
        this.children.addAll(children);
        this.executors.putAll(executors);
    }

    @Override
    public CommandState name(String name) {
        return new CommandState(name, routesBefore, aliases, children, executors, meta, canceled);
    }

    public CommandState routeBefore(Route route) {
        ArrayList<Route> routes = new ArrayList<>(routesBefore);
        routes.add(route);

        return new CommandState(name, routes, aliases, children, executors, meta, canceled);
    }

    public CommandState alias(String... alias) {
        Set<String> aliases = new HashSet<>(this.aliases);
        Collections.addAll(aliases, alias);

        return new CommandState(name, routesBefore, aliases, children, executors, meta, canceled);
    }

    public CommandState child(CommandState... child) {
        Set<CommandState> children = new HashSet<>(this.children);
        Collections.addAll(children, child);

        return new CommandState(name, routesBefore, aliases, children, executors, meta, canceled);
    }

    public CommandState executor(Method method, CommandState state) {
        Map<Method, CommandState> executors = new HashMap<>(this.executors);
        executors.put(method, state);

        return new CommandState(name, routesBefore, aliases, children, executors, meta, canceled);
    }

    public CommandState validator(Function<CountValidator, CountValidator> edit) {
        return this.meta(commandMeta -> commandMeta.applyCountValidator(edit));
    }

    public String getName() {
        return name;
    }

    public List<Route> getRoutesBefore() {
        return Collections.unmodifiableList(routesBefore);
    }

    public Set<String> getAliases() {
        return Collections.unmodifiableSet(aliases);
    }

    public Set<CommandState> getChildren() {
        return Collections.unmodifiableSet(children);
    }

    public Map<Method, CommandState> getExecutors() {
        return Collections.unmodifiableMap(executors);
    }

    public CommandState mergeMethod(Method method, CommandState other) {
        return this.mergeMethod(other.routesBefore.iterator(), method, other);
    }

    private CommandState mergeMethod(Iterator<Route> iterator, Method method, CommandState last) {
        if (!iterator.hasNext()) {
            if (last.getName() != null && !last.getName().isEmpty()) {
                CommandState before = new CommandState().name(last.getName()).alias(last.getAliases());
                CommandState withExecutor = before.executor(method, last);

                return this.child(withExecutor);
            }

            return this.executor(method, last);
        }

        Route route = iterator.next();
        CommandState commandState = new CommandState().name(route.getName()).alias(route.getAliases());

        commandState = commandState.mergeMethod(iterator, method, last);

        return this.child(commandState);
    }

    public CommandState alias(Collection<String> aliases) {
        Set<String> aliasesSet = new HashSet<>(this.aliases);
        aliasesSet.addAll(aliases);

        return new CommandState(name, routesBefore, aliasesSet, children, executors, meta, canceled);
    }

    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public CommandEditor.State editChild(String child, CommandEditor editor) {
        for (CommandState state : new HashSet<>(this.children)) {
            if (!state.name.equalsIgnoreCase(child)) {
                continue;
            }

            CommandEditor.State edited = editor.edit(state);

            if (!(edited instanceof CommandState)) {
                throw new IllegalStateException("State must be instance of CommandState");
            }

            this.children.remove(state);
            this.children.add((CommandState) edited);
            break;
        }

        return this;
    }

    @Override
    public CommandState aliases(Collection<String> aliases, boolean removeOld) {
        Set<String> aliasesSet = removeOld ? new HashSet<>() : new HashSet<>(this.aliases);
        aliasesSet.addAll(aliases);

        return new CommandState(name, routesBefore, aliasesSet, children, executors, meta, canceled);
    }

    @Override
    public CommandState permission(Collection<String> permissions, boolean removeOld) {
        return this.meta(commandMeta -> {
            if (removeOld) {
                commandMeta.clearPermissions();
            }

            commandMeta.addPermission(permissions);
            return commandMeta;
        });
    }

    @Override
    public CommandState permissionExcluded(Collection<String> executedPermissions, boolean removeOld) {
        return this.meta(commandMeta -> {
            if (removeOld) {
                commandMeta.clearExcludedPermissions();
            }

            commandMeta.addExcludedPermission(executedPermissions);
            return commandMeta;
        });
    }

    @Override
    public CommandState cancel(boolean canceled) {
        return new CommandState(name, routesBefore, aliases, children, executors, meta, canceled);
    }

    @Override
    public CommandState meta(Function<CommandMeta, CommandMeta> edit) {
        CommandMeta newMeta = CommandMeta.create();
        newMeta.applyCommandMeta(this.meta);

        CommandMeta metaData = edit.apply(newMeta);

        return new CommandState(name, routesBefore, aliases, children, executors, metaData, canceled);
    }

    public CommandMeta getMeta() {
        return meta;
    }

    public static class Route {
        private final String name;
        private final List<String> aliases = new ArrayList<>();

        public Route(String name, List<String> aliases) {
            this.name = name;
            this.aliases.addAll(aliases);
        }

        public String getName() {
            return name;
        }

        public List<String> getAliases() {
            return Collections.unmodifiableList(aliases);
        }
    }

}
