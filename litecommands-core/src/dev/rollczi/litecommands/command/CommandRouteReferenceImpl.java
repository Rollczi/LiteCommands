package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class CommandRouteReferenceImpl<SENDER> implements CommandRoute<SENDER> {

    private final UUID uniqueId = UUID.randomUUID();

    private final String name;
    private final List<String> aliases;
    private final List<String> names = new ArrayList<>();

    private final CommandRoute<SENDER> reference;

    CommandRouteReferenceImpl(String name, List<String> aliases, CommandRoute<SENDER> reference) {
        this.name = name;
        this.aliases = aliases;
        this.reference = reference;
        this.names.add(name);
        this.names.addAll(aliases);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public List<String> getAliases() {
        return Collections.unmodifiableList(this.aliases);
    }

    @Override
    public List<String> names() {
        return Collections.unmodifiableList(this.names);
    }

    @Override
    public boolean isNameOrAlias(String name) {
        return this.names.contains(name);
    }

    @Override
    public CommandRoute<SENDER> getParent() {
        return reference.getParent();
    }

    @Override
    public boolean isReference() {
        return true;
    }

    @Override
    public void appendChildren(CommandRoute<SENDER> children) {
        reference.appendChildren(children);
    }

    @Override
    public List<CommandRoute<SENDER>> getChildren() {
        return reference.getChildren();
    }

    @Override
    public Optional<CommandRoute<SENDER>> getChild(String name) {
        return reference.getChild(name);
    }

    @Override
    public void appendExecutor(CommandExecutor<SENDER> executor) {
        reference.appendExecutor(executor);
    }

    @Override
    public List<CommandExecutor<SENDER>> getExecutors() {
        return reference.getExecutors();
    }

    @Override
    public Meta meta() {
        return reference.meta();
    }

    @Override
    public @Nullable MetaHolder parentMeta() {
        return reference.parentMeta();
    }

}
