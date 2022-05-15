package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.command.permission.LitePermissions;
import dev.rollczi.litecommands.command.sugesstion.Suggestion;
import dev.rollczi.litecommands.command.FindResult;
import dev.rollczi.litecommands.command.amount.AmountValidator;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.command.sugesstion.SuggestResult;
import dev.rollczi.litecommands.command.execute.ExecuteResult;
import dev.rollczi.litecommands.command.execute.ArgumentExecutor;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.platform.LiteSender;
import panda.utilities.ValidationUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

class LiteCommandSection implements CommandSection {

    private final String name;

    private final Set<String> aliases = new HashSet<>();
    private final List<CommandSection> childSections = new ArrayList<>();
    private final List<ArgumentExecutor> argumentExecutors = new ArrayList<>();

    private final Set<String> permissions = new HashSet<>();
    private final Set<String> excludedPermissions = new HashSet<>();

    private AmountValidator amountValidator = AmountValidator.NONE;

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
    public List<Suggestion> suggestions() {
        Set<String> suggestions = new HashSet<>(this.aliases);
        suggestions.add(this.name);

        return Suggestion.of(suggestions);
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
            Optional<Object> result = findResult.getResult();

            return result
                    .map(o -> ExecuteResult.invalid(findResult, o))
                    .orElseGet(() -> ExecuteResult.failure(findResult));
        }

        return ExecuteResult.failure(findResult);
    }

    @Override
    public SuggestResult suggestion(LiteInvocation invocation) {
        FindResult findResult = this.find(invocation, 0, FindResult.none(invocation));
        List<Suggestion> lastSuggestion = findResult.knownSuggestion();

        return SuggestResult.of(lastSuggestion);
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

        LitePermissions litePermissions = LitePermissions.of(this, invocation.sender());

        if (!litePermissions.isEmpty()) {
            return lastResult.withSection(this)
                    .invalid(litePermissions);
        }

        for (ArgumentExecutor argumentExecutor : argumentExecutors) {
            FindResult findResult = argumentExecutor.find(invocation, route + 1, lastResult.withSection(this));

            if (findResult.isFound()) {
                return findResult;
            }

            if (last == null || findResult.isLongerThan(last)) {
                last = findResult;
            }
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

        this.applySettings(section);
        this.aliases.addAll(section.getAliases());
    }

    @Override
    public void executor(ArgumentExecutor argumentExecutor) {
        argumentExecutors.add(argumentExecutor);
    }

    @Override
    public void permission(String... permissions) {
        this.permissions.addAll(Arrays.asList(permissions));
    }

    @Override
    public void permission(Collection<String> permissions) {
        this.permissions.addAll(permissions);
    }

    @Override
    public Collection<String> permissions() {
        return Collections.unmodifiableSet(permissions);
    }

    @Override
    public void excludePermission(String... permissions) {
        this.excludedPermissions.addAll(Arrays.asList(permissions));
    }

    @Override
    public void excludePermission(Collection<String> permissions) {
        this.excludedPermissions.addAll(permissions);
    }

    @Override
    public Collection<String> excludePermissions() {
        return Collections.unmodifiableSet(excludedPermissions);
    }

    @Override
    public void amountValidator(AmountValidator validator) {
        this.amountValidator = validator;
    }

    @Override
    public void applyOnValidator(Function<AmountValidator, AmountValidator> edit) {
        this.amountValidator = edit.apply(this.amountValidator);
    }

    @Override
    public AmountValidator amountValidator() {
        return this.amountValidator;
    }

    @Override
    public List<CommandSection> childrenSection() {
        return Collections.unmodifiableList(childSections);
    }

    @Override
    public List<ArgumentExecutor> executors() {
        return Collections.unmodifiableList(argumentExecutors);
    }

}
