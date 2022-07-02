package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.argument.AnnotatedParameter;
import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.platform.LiteSender;
import dev.rollczi.litecommands.sugesstion.Suggester;
import dev.rollczi.litecommands.sugesstion.SuggesterResult;
import dev.rollczi.litecommands.sugesstion.SuggestionMerger;
import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.command.permission.LitePermissions;
import dev.rollczi.litecommands.sugesstion.Suggestion;
import dev.rollczi.litecommands.command.FindResult;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.sugesstion.SuggestionStack;
import dev.rollczi.litecommands.sugesstion.UniformSuggestionStack;
import dev.rollczi.litecommands.command.execute.ExecuteResult;
import dev.rollczi.litecommands.command.execute.ArgumentExecutor;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.shared.Validation;
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

    private final CommandMeta meta = new LiteCommandMeta();

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

        if (invocation.arguments().length == route) {
            return suggestionMerger.appendRoot(this.suggestion());
        }

        int routeAbove = route + 1;

        root:
        for (CommandSection<SENDER> section : this.childSections) {
            for (String permission : section.meta().getPermissions()) {
                LiteSender sender = invocation.sender();

                if (!sender.hasPermission(permission)) {
                    continue root;
                }
            }

            SuggesterResult result = section.extractSuggestions(route, invocation.toLite());

            if (result.isFailure()) {
                continue;
            }

            suggestionMerger.appendRoot(section.findSuggestion(invocation, routeAbove).merge());
        }

        LiteInvocation lite = invocation.toLite();

        for (ArgumentExecutor<SENDER> argumentExecutor : this.argumentExecutors) {
            List<AnnotatedParameter<SENDER, ?>> parameters = argumentExecutor.annotatedParameters();

            suggestionMerger.appendRoot(this.suggestionParameters(lite, 0, routeAbove, 0, parameters));
        }

        return suggestionMerger;
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

        LitePermissions missingSection = LitePermissions.of(this.meta, invocation.sender());

        for (ArgumentExecutor<SENDER> argumentExecutor : argumentExecutors) {
            FindResult<SENDER> findResult = argumentExecutor.find(invocation, route + 1, lastResult.withSection(this));

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
