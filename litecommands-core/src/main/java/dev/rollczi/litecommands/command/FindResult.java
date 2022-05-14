package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.argument.AnnotatedParameter;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.AnnotatedParameterState;
import dev.rollczi.litecommands.command.execute.ArgumentExecutor;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.shared.Validation;
import dev.rollczi.litecommands.command.sugesstion.Suggester;
import dev.rollczi.litecommands.command.sugesstion.Suggestion;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class FindResult {

    private final LiteInvocation invocation;

    private final List<CommandSection> sections;
    private final ArgumentExecutor executor;
    private final List<AnnotatedParameter<?>> allArguments;
    private final List<AnnotatedParameterState<?>> arguments;

    private final @Nullable AnnotatedParameterState<?> lastMatchedArgument;
    private final @Nullable MatchResult failedResult;

    private final boolean found;

    // See failedResult (MatchResult)
    @Deprecated
    private final @Nullable AnnotatedParameterState<?> failedArgument;
    @Deprecated
    private final boolean failed;
    @Deprecated
    private final boolean invalid;

    private FindResult(
            LiteInvocation invocation,
            List<CommandSection> sections,
            ArgumentExecutor executor,
            List<AnnotatedParameter<?>> allArguments,
            List<AnnotatedParameterState<?>> arguments,
            @Nullable AnnotatedParameterState<?> lastMatchedArgument,
            @Nullable AnnotatedParameterState<?> failedArgument,
            @Nullable MatchResult failedResult, boolean found, boolean failed, boolean invalid
    ) {
        this.invocation = invocation;
        this.sections = sections;
        this.executor = executor;
        this.allArguments = allArguments;
        this.arguments = arguments;
        this.lastMatchedArgument = lastMatchedArgument;
        this.failedArgument = failedArgument;
        this.failedResult = failedResult;
        this.found = found;
        this.failed = failed;
        this.invalid = invalid;
    }

    public FindResult withSection(CommandSection section) {
        Validation.isNull(this.executor, "Executor is set");

        List<CommandSection> sections = new ArrayList<>(this.sections);

        sections.add(section);
        return new FindResult(invocation, sections, null, allArguments, arguments, lastMatchedArgument, failedArgument, failedResult, found, failed, invalid);
    }

    public FindResult withExecutor(ArgumentExecutor executor, List<AnnotatedParameter<?>> argumentStates) {
        Validation.isNull(this.executor, "Executor already set");
        Validation.isFalse(this.found, "This is the end of the command");
        Validation.isNotEmpty(this.sections, "Executor must have a parent section");

        if (sections.isEmpty()) {
            throw new IllegalStateException();
        }

        for (ArgumentExecutor exec : sections.get(sections.size() - 1).executors()) {
            if (exec.equals(executor)) {
                return new FindResult(invocation, sections, executor, argumentStates, arguments, lastMatchedArgument, failedArgument, failedResult, false, failed, invalid);
            }
        }

        throw new IllegalArgumentException("Executor not found in last section.");
    }

    public FindResult withArgument(AnnotatedParameterState<?> state, boolean matched) {
        Validation.isNotNull(this.executor, "Executor not set");
        Validation.isFalse(this.found, "FindResult is found");
        Validation.isFalse(this.failed, "FindResult is failed");
        Validation.isFalse(this.invalid, "FindResult is invalid");

        List<AnnotatedParameterState<?>> arguments = new ArrayList<>(this.arguments);

        arguments.add(state);
        return new FindResult(invocation, sections, executor, allArguments, arguments, matched ? state : this.lastMatchedArgument, failedArgument, failedResult, false, false, false);
    }

    public FindResult failedArgument(AnnotatedParameterState<?> state) {
        Validation.isNotNull(this.executor, "Executor not set");
        Validation.isFalse(this.found, "FindResult is found");
        Validation.isFalse(this.failed, "FindResult is failed");
        Validation.isFalse(this.invalid, "FindResult is invalid");

        return new FindResult(invocation, sections, executor, allArguments, arguments, lastMatchedArgument, state, state.matchResult(), false, true, false);
    }

    public FindResult invalidArgument(AnnotatedParameterState<?> state) {
        Validation.isNotNull(this.executor, "Executor not set");
        Validation.isFalse(this.found, "FindResult is found");
        Validation.isFalse(this.failed, "FindResult is failed");
        Validation.isFalse(this.invalid, "FindResult is invalid");

        return new FindResult(invocation, sections, executor, allArguments, arguments, lastMatchedArgument, state, state.matchResult(), false, false, true);
    }

    public FindResult invalid() {
        Validation.isNotNull(this.executor, "Executor not set");
        Validation.isFalse(this.found, "FindResult is found");
        Validation.isFalse(this.failed, "FindResult is failed");
        Validation.isFalse(this.invalid, "FindResult is invalid");

        return new FindResult(invocation, sections, executor, allArguments, arguments, lastMatchedArgument, failedArgument, MatchResult.notMatched(), false, false, true);
    }

    public FindResult markAsFound() {
        Validation.isFalse(this.found, "FindResult is found");
        Validation.isFalse(this.failed, "FindResult is failed");
        Validation.isFalse(this.invalid, "FindResult is invalid");

        return new FindResult(invocation, sections, executor, allArguments, arguments, lastMatchedArgument, failedArgument, failedResult, true, false, false);
    }

    @Deprecated
    public Map<Integer, CommandSection> getSectionsAsMap() {
        Map<Integer, CommandSection> sectionMap = new HashMap<>();

        int index = 0;
        for (CommandSection section : sections) {
            sectionMap.put(index++, section);
        }

        return Collections.unmodifiableMap(sectionMap);
    }

    public List<CommandSection> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public Optional<ArgumentExecutor> getExecutor() {
        return Optional.ofNullable(executor);
    }

    public List<AnnotatedParameter<?>> getAllArguments() {
        return Collections.unmodifiableList(allArguments);
    }

    public List<Argument<?>> getArguments() {
        return this.arguments.stream()
                .map(AnnotatedParameter::argument)
                .collect(Collectors.toList());
    }

    @Deprecated
    public Optional<Object> getInvalidResult() {
        return Optional.ofNullable(this.failedArgument).flatMap(state -> state.matchResult().getNoMatchedResult());
    }

    public List<Object> extractResults() {
        Validation.isTrue(this.found, "FindResult must be found");

        List<Object> results = new ArrayList<>();

        for (AnnotatedParameterState<?> state : arguments) {
            MatchResult matchResult = state.matchResult();

            if (matchResult.isNotMatched()) {
                if (!state.argument().isOptional()) {
                    throw new IllegalStateException("Can't extract result from not matched FindResult");
                }

                results.addAll(state.argument().getDefault());
            }

            results.addAll(state.result());
        }

        return Collections.unmodifiableList(results);
    }

    public List<Suggestion> knownSuggestion() {
        List<String> arguments = new ArrayList<>();
        arguments.add(this.invocation.label());
        arguments.addAll(Arrays.asList(this.invocation.arguments()));

        List<Suggester> suggesters = new ArrayList<>();

        suggesters.add(() -> this.sections.get(0).suggestions());

        for (CommandSection section : this.sections) {
            List<Suggestion> suggestions = new ArrayList<>();

            for (CommandSection childSection : section.childrenSection()) {
                suggestions.addAll(childSection.suggestions());
            }

            for (ArgumentExecutor argumentExecutor : section.executors()) {
//                if (executor != null && !executor.equals(argumentExecutor) && !arguments.isEmpty() && !arguments.get(arguments.size() - 1).isEmpty()) {
//                    continue;
//                }

                for (AnnotatedParameter<?> parameter : argumentExecutor.annotatedParameters()) {
                    suggestions.addAll(parameter.toSuggester(invocation).suggestions());

                    if (!parameter.argument().isOptional()) {
                        break;
                    }
                }
            }

            suggesters.add(Suggester.of(suggestions));
        }

        for (int index = 1; index < allArguments.size(); index++) {
            AnnotatedParameter<?> parameter = allArguments.get(index);
            Suggester suggester = parameter.toSuggester(invocation);

            if (!parameter.argument().isOptional()) {
                suggesters.add(Suggester.of(suggester.suggestions()));
                continue;
            }

            List<Suggestion> suggestions = new ArrayList<>(suggester.suggestions());

            for (int optional = index + 1; optional < allArguments.size(); optional++) {
                AnnotatedParameter<?> annotatedParameter = allArguments.get(optional);
                Suggester optionalSuggester = annotatedParameter.toSuggester(invocation);

                suggestions.addAll(optionalSuggester.suggestions());

                if (!annotatedParameter.argument().isOptional()) {
                    break;
                }
            }

            suggesters.add(Suggester.of(suggestions));
        }

        return known(arguments.iterator(), suggesters.iterator(), Suggester.NONE, Collections.emptyList());
    }

    private List<Suggestion> known(Iterator<String> arguments, Iterator<Suggester> suggesters, Suggester lastIterated, List<Suggestion> multilevelSuggestions) {
        if (!arguments.hasNext()) {
            return multilevelSuggestions.isEmpty()
                    ? lastIterated.suggestions()
                    : Collections.unmodifiableList(multilevelSuggestions);
        }

        if (!suggesters.hasNext()) {
            while (arguments.hasNext()) {
                arguments.next();
                multilevelSuggestions = multilevelSuggestions.stream()
                        .filter(Suggestion::isMultilevel)
                        .map(suggestion -> suggestion.slashLevel(1))
                        .collect(Collectors.toList());
            }

            return multilevelSuggestions;
        }

        String next = arguments.next();

        List<Suggestion> additional = multilevelSuggestions.stream()
                .filter(Suggestion::isMultilevel)
                .map(suggestion -> suggestion.slashLevel(1))
                .collect(Collectors.toList());

        if (additional.isEmpty()) {
            lastIterated = suggesters.next();
            additional = lastIterated.suggestions().stream()
                    .filter(suggestion -> suggestion.multilevel().toLowerCase().startsWith(next.toLowerCase())) //TODO: Dynamic validation
                    .collect(Collectors.toList());
        }

        return known(arguments, suggesters, lastIterated, additional);
    }

    public boolean isLongerThan(FindResult findResult) {
        if (this.isFailed() && !findResult.isFailed()) {
            return false;
        }

        if (this.isFound() && !findResult.isFound()) {
            return true;
        }

        if (this.sections.size() > findResult.sections.size()) {
            return true;
        }

        if (this.executor != null && findResult.executor == null) {
            return true;
        }

        return this.arguments.size() > findResult.arguments.size();
    }

    public boolean isFound() {
        return found;
    }

    public boolean isNotFound() {
        return !found;
    }

    @Deprecated
    public boolean isFailed() {
        return failed;
    }

    @Deprecated
    public boolean isInvalid() {
        return invalid;
    }

    public static FindResult none(LiteInvocation invocation) {
        return new FindResult(invocation, new ArrayList<>(), null, new ArrayList<>(), new ArrayList<>(), null, null, null, false, false, false);
    }

}
