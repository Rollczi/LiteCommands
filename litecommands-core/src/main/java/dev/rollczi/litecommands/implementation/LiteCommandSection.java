package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.argument.AnnotatedParameter;
import dev.rollczi.litecommands.command.FindResult;
import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.execute.ArgumentExecutor;
import dev.rollczi.litecommands.command.execute.ExecuteResult;
import dev.rollczi.litecommands.command.permission.RequiredPermissions;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.platform.LiteSender;
import dev.rollczi.litecommands.shared.Validation;
import dev.rollczi.litecommands.suggestion.Suggester;
import dev.rollczi.litecommands.suggestion.SuggesterResult;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionMerger;
import dev.rollczi.litecommands.suggestion.SuggestionStack;
import dev.rollczi.litecommands.suggestion.UniformSuggestionStack;
import panda.std.Option;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

class LiteCommandSection<SENDER> implements CommandSection<SENDER> {

    private final String name;

    private final Set<String> aliases = new HashSet<>();
    private final List<CommandSection<SENDER>> childSections = new ArrayList<>();
    private final List<ArgumentExecutor<SENDER>> argumentExecutors = new ArrayList<>();

    private final CommandMeta meta = CommandMeta.create();

    LiteCommandSection(String name, Collection<String> aliases) {
        Validation.isNotNull(name, "name");
        Validation.isNotNull(aliases, "aliases");

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
    public UniformSuggestionStack suggestion() {
        Set<String> suggestions = new HashSet<>(this.aliases);
        suggestions.add(this.name);

        return UniformSuggestionStack.of(Suggestion.of(suggestions));
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
    public ExecuteResult execute(Invocation<SENDER> invocation) {
        FindResult<SENDER> findResult = this.find(invocation.toLite(), 0, FindResult.none(invocation));
        Optional<ArgumentExecutor<SENDER>> executor = findResult.getExecutor();

        if (executor.isPresent()) {
            ArgumentExecutor<SENDER> argumentExecutor = executor.get();

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
    public SuggestionMerger findSuggestion(Invocation<SENDER> invocation, int route) {
        SuggestionMerger suggestionMerger = SuggestionMerger.empty(invocation);
        LiteSender sender = invocation.sender();

        for (String permission : this.meta.getPermissions()) {
            if (!sender.hasPermission(permission)) {
                return suggestionMerger;
            }
        }

        if (!this.hasPermissionToAnyExecutor(invocation.sender())) {
            return suggestionMerger;
        }

        if (invocation.arguments().length == route) {
            return suggestionMerger.appendRoot(this.suggestion());
        }

        int routeAbove = route + 1;

        for (CommandSection<SENDER> section : this.childSections) {
            SuggesterResult result = section.extractSuggestions(route, invocation.toLite());

            if (result.isFailure()) {
                continue;
            }

            suggestionMerger.appendRoot(section.findSuggestion(invocation, routeAbove).merge());
        }

        LiteInvocation lite = invocation.toLite();

        for (ArgumentExecutor<SENDER> argumentExecutor : this.argumentExecutors) {
            RequiredPermissions requiredPermissions = RequiredPermissions.of(argumentExecutor.meta(), lite.sender());

            if (!requiredPermissions.isEmpty()) {
                continue;
            }

            List<AnnotatedParameter<SENDER, ?>> parameters = argumentExecutor.annotatedParameters();

            suggestionMerger.appendRoot(this.suggestionParameters(lite, 0, routeAbove, 0, parameters));
        }

        return suggestionMerger;
    }

    private boolean hasPermissionToAnyExecutor(LiteSender liteSender) {
        if (this.executors().isEmpty()) {
            return true;
        }

        for (ArgumentExecutor<SENDER> executor : this.argumentExecutors) {
            Collection<String> permissions = executor.meta().getPermissions();

            if (permissions.isEmpty()) {
                return true;
            }

            for (String permission : permissions) {
                if (liteSender.hasPermission(permission)) {
                    return true;
                }
            }
        }

        return false;
    }

    private SuggestionStack suggestionParameters(LiteInvocation lite, int margin, int routeAbove, int parameterIndex, List<AnnotatedParameter<SENDER, ?>> parameters) {
        List<AnnotatedParameter<SENDER, ?>> list = parameters.subList(parameterIndex, parameters.size());
        int routeReal = routeAbove + parameterIndex;

        if (list.isEmpty() || routeReal > lite.arguments().length) {
            return SuggestionStack.empty();
        }

        AnnotatedParameter<SENDER, ?> parameter = list.get(0);
        Suggester suggester = parameter.toSuggester(lite, routeAbove);
        SuggesterResult result = suggester.extractSuggestions(routeReal - 1 + margin, lite);

        if (result.isFailure()) {
            return SuggestionStack.empty();
        }

        SuggestionMerger merger = SuggestionMerger.empty(lite);

        UniformSuggestionStack suggest = result.getSuggestions();
        int nextMargin = margin + suggest.lengthMultilevel() - 1;

        if (!suggest.isEmpty()) {
            merger.append(routeReal + margin, suggest);
        }

        SuggestionStack stack = suggestionParameters(lite, nextMargin, routeAbove, parameterIndex + 1, parameters);

        merger.appendRoot(stack);

        if (parameter.argument().isOptional()) {
            SuggestionStack optionalSuggestions = this.suggestionParameters(lite, nextMargin, routeAbove - 1, parameterIndex + 1, parameters);

            merger.append(routeReal, optionalSuggestions);
        }

        return merger.merge();
    }

    @Override
    public FindResult<SENDER> find(LiteInvocation invocation, int route, FindResult<SENDER> lastResult) {
        Optional<String> optional = invocation.argument(route);

        FindResult<SENDER> last = null;

        if (optional.isPresent()) {
            String argument = optional.get();

            for (CommandSection<SENDER> commandSection : childSections) {
                if (!commandSection.isSimilar(argument)) {
                    continue;
                }

                FindResult<SENDER> findResult = commandSection.find(invocation, route + 1, lastResult.withSection(this));

                if (findResult.isFound()) {
                    return findResult;
                }

                if (last == null || findResult.isLongerThan(last)) {
                    last = findResult;
                }
            }
        }

        RequiredPermissions missingSection = RequiredPermissions.of(this.meta, invocation.sender());

        for (ArgumentExecutor<SENDER> argumentExecutor : argumentExecutors) {
            FindResult<SENDER> findResult = argumentExecutor.find(invocation, route + 1, lastResult.withSection(this));

            if (findResult.isFound()) {
                RequiredPermissions missingExecutor = RequiredPermissions.of(argumentExecutor.meta(), invocation.sender());

                if (!missingSection.isEmpty() || !missingExecutor.isEmpty()) {
                    if (last != null && last.getResult().is(RequiredPermissions.class).isPresent()) {
                        return last;
                    }

                    RequiredPermissions all = missingSection.with(missingExecutor);

                    return lastResult.withSection(this)
                            .invalid(all);
                }

                return findResult;
            }

            last = this.resolveCurrentAndLast(findResult, last);
        }

        if (!missingSection.isEmpty()) {
            return lastResult.withSection(this)
                    .invalid(missingSection);
        }

        return last != null ? last : lastResult.withSection(this);
    }

    private FindResult<SENDER> resolveCurrentAndLast(FindResult<SENDER> current, FindResult<SENDER> last) {
        if (last == null) {
            return current;
        }

        if (!current.isLongerThan(last)) {
            return last;
        }

        Option<RequiredPermissions> optionCurrent = current.getResult().is(RequiredPermissions.class);
        Option<RequiredPermissions> optionLast = last.getResult().is(RequiredPermissions.class);

        if (optionCurrent.isPresent() && optionLast.isEmpty()) {
            RequiredPermissions currentPermission = optionCurrent.get();
            RequiredPermissions lastPermissions = optionLast.get();

            return current.withResult(currentPermission.with(lastPermissions));
        }
        else if (optionLast.isEmpty()) {
            return current;
        }

        return last;
    }

    @Override
    public void childSection(CommandSection<SENDER> section) {
        for (CommandSection<SENDER> commandSection : new ArrayList<>(childSections)) {
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
    public void mergeSection(CommandSection<SENDER> section) {
        if (!section.getName().equalsIgnoreCase(this.name)) {
            throw new IllegalArgumentException("Cannot merge sections with different names.");
        }

        for (CommandSection<SENDER> child : section.childrenSection()) {
            this.childSection(child);
        }

        for (ArgumentExecutor<SENDER> argumentExecutor : section.executors()) {
            this.executor(argumentExecutor);
        }

        this.meta().applyCommandMeta(section.meta());
        this.aliases.addAll(section.getAliases());
    }

    @Override
    public void executor(ArgumentExecutor<SENDER> argumentExecutor) {
        this.argumentExecutors.add(argumentExecutor);
    }

    @Override
    public List<CommandSection<SENDER>> childrenSection() {
        return Collections.unmodifiableList(childSections);
    }

    @Override
    public List<ArgumentExecutor<SENDER>> executors() {
        return Collections.unmodifiableList(argumentExecutors);
    }

    @Override
    public CommandMeta meta() {
        return this.meta;
    }

}
