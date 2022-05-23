package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.command.meta.Meta;
import dev.rollczi.litecommands.command.permission.LitePermissions;
import dev.rollczi.litecommands.command.sugesstion.Suggestion;
import dev.rollczi.litecommands.command.FindResult;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.command.sugesstion.SuggestionStack;
import dev.rollczi.litecommands.command.sugesstion.TwinSuggestionStack;
import dev.rollczi.litecommands.command.execute.ExecuteResult;
import dev.rollczi.litecommands.command.execute.ArgumentExecutor;
import dev.rollczi.litecommands.command.LiteInvocation;
import panda.std.Option;
import panda.utilities.ValidationUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

class LiteCommandSection implements CommandSection {

    private final String name;

    private final Set<String> aliases = new HashSet<>();
    private final List<CommandSection> childSections = new ArrayList<>();
    private final List<ArgumentExecutor> argumentExecutors = new ArrayList<>();

    private final Meta meta = new LiteMeta();

    LiteCommandSection(String name, Collection<String> aliases) {
        ValidationUtils.notNull(name, "name");
        ValidationUtils.notNull(aliases, "aliases");

        this.name = name;
        this.aliases.addAll(aliases);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Set<String> getAliases() {
        return this.aliases;
    }

    @Override
    public TwinSuggestionStack suggest() {
        Set<String> suggestions = new HashSet<>(this.aliases);
        suggestions.add(this.name);

        return TwinSuggestionStack.of(Suggestion.of(suggestions));
    }

    @Override
    public boolean isSimilar(String name) {
        if (this.name.equalsIgnoreCase(name)) {
            return true;
        }

        for (String alias : aliases) {
            if (alias.equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public ExecuteResult execute(LiteInvocation invocation) {
        FindResult findResult = this.find(invocation, 0, FindResult.none(invocation));
        Optional<ArgumentExecutor> executor = findResult.getExecutor();

        if (executor.isPresent()) {
            ArgumentExecutor argumentExecutor = executor.get();

            return argumentExecutor.execute(invocation, findResult);
        }

        if (findResult.isInvalid()) {
            Option<Object> result = findResult.getResult();

            return result
                    .map(o -> ExecuteResult.invalid(findResult, o))
                    .orElseGet(() -> ExecuteResult.failure(findResult));
        }

        return ExecuteResult.failure(findResult);
    }

    @Override
    public SuggestionStack suggestion(LiteInvocation invocation) {
        FindResult findResult = this.find(invocation, 0, FindResult.none(invocation));

        return findResult.knownSuggestion();
    }

    @Override
    public FindResult find(LiteInvocation invocation, int route, FindResult lastResult) {
        Optional<String> optional = invocation.argument(route);

        FindResult last = null;

        if (optional.isPresent()) {
            String argument = optional.get();

            for (CommandSection commandSection : childSections) {
                if (!commandSection.isSimilar(argument)) {
                    continue;
                }

                FindResult findResult = commandSection.find(invocation, route + 1, lastResult.withSection(this));

                if (findResult.isFound()) {
                    return findResult;
                }

                if (last == null || findResult.isLongerThan(last)) {
                    last = findResult;
                }
            }
        }

        LitePermissions missingSection = LitePermissions.of(this.meta, invocation.sender());

        for (ArgumentExecutor argumentExecutor : argumentExecutors) {
            FindResult findResult = argumentExecutor.find(invocation, route + 1, lastResult.withSection(this));

            if (findResult.isFound()) {
                LitePermissions missingExecutor = LitePermissions.of(argumentExecutor.meta(), invocation.sender());

                if (!missingSection.isEmpty() || !missingExecutor.isEmpty()) {
                    if (last != null && last.getResult().is(LitePermissions.class).isPresent()) {
                        return last;
                    }

                    LitePermissions all = missingSection.with(missingExecutor);

                    return lastResult.withSection(this)
                            .invalid(all);
                }

                return findResult;
            }

            if (last == null || findResult.isLongerThan(last)) {
                last = findResult;
            }
        }

        if (!missingSection.isEmpty()) {
            return lastResult.withSection(this)
                    .invalid(missingSection);
        }

        return last != null ? last : lastResult.withSection(this);
    }

    @Override
    public void childSection(CommandSection section) {
        for (CommandSection commandSection : new ArrayList<>(childSections)) {
            if (commandSection.isSimilar(section.getName())) {
                commandSection.mergeSection(section);
                return;
            }

            for (String alias : section.getAliases()) {
                if (commandSection.isSimilar(alias)) {
                    throw new IllegalArgumentException("Command section with alias " + alias + " already exists.");
                }
            }
        }

        this.childSections.add(section);
    }

    @Override
    public void mergeSection(CommandSection section) {
        if (!section.getName().equalsIgnoreCase(this.name)) {
            throw new IllegalArgumentException("Cannot merge sections with different names.");
        }

        for (CommandSection child : section.childrenSection()) {
            this.childSection(child);
        }

        for (ArgumentExecutor argumentExecutor : section.executors()) {
            this.executor(argumentExecutor);
        }

        this.meta().apply(section.meta());
        this.aliases.addAll(section.getAliases());
    }

    @Override
    public void executor(ArgumentExecutor argumentExecutor) {
        argumentExecutors.add(argumentExecutor);
    }

    @Override
    public List<CommandSection> childrenSection() {
        return Collections.unmodifiableList(childSections);
    }

    @Override
    public List<ArgumentExecutor> executors() {
        return Collections.unmodifiableList(argumentExecutors);
    }

    @Override
    public Meta meta() {
        return this.meta;
    }

}
