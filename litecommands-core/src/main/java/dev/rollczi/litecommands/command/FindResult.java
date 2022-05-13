package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.argument.AnnotatedParameter;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.AnnotatedParameterState;
import dev.rollczi.litecommands.command.execute.ArgumentExecutor;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.shared.Validation;
import dev.rollczi.litecommands.command.sugesstion.Suggester;
import dev.rollczi.litecommands.command.sugesstion.Suggestion;

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

    private final AnnotatedParameterState<?> lastMatchedArgument;
    private final AnnotatedParameterState<?> failedArgument;

    private final boolean found;
    private final boolean failed;
    private final boolean invalid;

    private FindResult(
            LiteInvocation invocation,
            List<CommandSection> sections,
            ArgumentExecutor executor,
            List<AnnotatedParameter<?>> allArguments,
            List<AnnotatedParameterState<?>> arguments,
            AnnotatedParameterState<?> lastMatchedArgument,
            AnnotatedParameterState<?> failedArgument,
            boolean found, boolean failed, boolean invalid
    ) {
        this.invocation = invocation;
        this.sections = sections;
        this.executor = executor;
        this.allArguments = allArguments;
        this.arguments = arguments;
        this.lastMatchedArgument = lastMatchedArgument;
        this.failedArgument = failedArgument;
        this.found = found;
        this.failed = failed;
        this.invalid = invalid;
    }

    public FindResult withSection(CommandSection section) {
        Validation.isNull(this.executor, "Executor is set");

        List<CommandSection> sections = new ArrayList<>(this.sections);

        sections.add(section);
        return new FindResult(invocation, sections, null, allArguments, arguments, lastMatchedArgument, failedArgument, found, failed, invalid);
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
                return new FindResult(invocation, sections, executor, argumentStates, arguments, lastMatchedArgument, failedArgument, false, failed, invalid);
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
        return new FindResult(invocation, sections, executor, allArguments, arguments, matched ? state : this.lastMatchedArgument, failedArgument, false, false, false);
    }

    public FindResult failedArgument(AnnotatedParameterState<?> state) {
        Validation.isNotNull(this.executor, "Executor not set");
        Validation.isFalse(this.found, "FindResult is found");
        Validation.isFalse(this.failed, "FindResult is failed");
        Validation.isFalse(this.invalid, "FindResult is invalid");

        return new FindResult(invocation, sections, executor, allArguments, arguments, lastMatchedArgument, state, false, true, false);
    }

    public FindResult invalidArgument(AnnotatedParameterState<?> state) {
        Validation.isNotNull(this.executor, "Executor not set");
        Validation.isFalse(this.found, "FindResult is found");
        Validation.isFalse(this.failed, "FindResult is failed");
        Validation.isFalse(this.invalid, "FindResult is invalid");

        return new FindResult(invocation, sections, executor, allArguments, arguments, lastMatchedArgument, state, false, false, true);
    }

    public FindResult invalid() {
        Validation.isNotNull(this.executor, "Executor not set");
        Validation.isFalse(this.found, "FindResult is found");
        Validation.isFalse(this.failed, "FindResult is failed");
        Validation.isFalse(this.invalid, "FindResult is invalid");

        return new FindResult(invocation, sections, executor, allArguments, arguments, lastMatchedArgument, failedArgument, false, false, true);
    }

    public FindResult markAsFound() {
        Validation.isFalse(this.found, "FindResult is found");
        Validation.isFalse(this.failed, "FindResult is failed");
        Validation.isFalse(this.invalid, "FindResult is invalid");

        return new FindResult(invocation, sections, executor, allArguments, arguments, lastMatchedArgument, failedArgument, true, false, false);
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

        List<Suggester> suggesters = new ArrayList<>(this.sections);

        if (executor == null && this.arguments.isEmpty()) {
            CommandSection last = this.sections.get(this.sections.size() - 1);

            suggesters.add(() -> {
                List<Suggestion> suggestions = new ArrayList<>();

                for (CommandSection section : last.childrenSection()) {
                    suggestions.addAll(section.suggestions());
                }

                for (ArgumentExecutor argumentExecutor : last.executors()) {
                    suggestions.addAll(argumentExecutor.firstSuggestions(invocation));
                }

                return suggestions;
            });
        }
        else {
            int argumentIndex = 0;
            for (AnnotatedParameterState<?> argument : this.arguments) {
                List<AnnotatedParameter<?>> suggestersIn = new ArrayList<>();
                suggestersIn.add(argument);

                for (int optionalIndex = argumentIndex + 1; optionalIndex < allArguments.size(); optionalIndex++) {
                    AnnotatedParameter<?> parameter = allArguments.get(optionalIndex);

                    if (!suggestersIn.get(suggestersIn.size() - 1).argument().isOptional()) {
                        break;
                    }

                    suggestersIn.add(parameter);
                }

                suggesters.add(() -> {
                    List<Suggestion> suggestions = new ArrayList<>();

                    for (AnnotatedParameter<?> suggester : suggestersIn) {
                        suggestions.addAll(suggester.toSuggester(invocation).suggestions());
                    }

                    return suggestions;
                });

                argumentIndex++;
            }

            if (allArguments.size() > arguments.size()) {
                for (int index = arguments.size(); index < allArguments.size(); index++) {
                    AnnotatedParameter<?> last = allArguments.get(arguments.size());

                    if (!last.argument().isOptional()) {
                        break;
                    }

                    suggesters.add(last.toSuggester(invocation));
                }
            }

            if (this.failedArgument != null) {
                suggesters.add(this.failedArgument);
            }
        }

        return known(arguments.iterator(), suggesters.iterator(), Suggester.NONE, Collections.emptyList());
    }

    private List<Suggestion> known(Iterator<String> arguments, Iterator<Suggester> suggesters, Suggester lastIterated, List<Suggestion> multilevelSuggestions) {
        if (!arguments.hasNext()) {
            if (!multilevelSuggestions.isEmpty()) {
                return Collections.unmodifiableList(multilevelSuggestions);
            }

            return lastIterated.suggestions();
        }

        if (!suggesters.hasNext()) {
            if (multilevelSuggestions.isEmpty()) {
                return Collections.emptyList();
            }

            List<Suggestion> multi = new ArrayList<>();

            for (Suggestion suggestion : multilevelSuggestions) {
                if (!suggestion.isMultilevel()) {
                    continue;
                }

                multi.add(suggestion.slashLevel(1));
            }

            return multi;
        }

        arguments.next();

        List<Suggestion> additional = new ArrayList<>();
        List<Suggestion> toCheck = new ArrayList<>();

        boolean handleMulti = !multilevelSuggestions.isEmpty();

        if (handleMulti) {
            toCheck.addAll(multilevelSuggestions);
        }
        else {
            lastIterated = suggesters.next();
            toCheck.addAll(lastIterated.suggestions());
        }

        for (Suggestion suggestion : toCheck) {
            if (!suggestion.isMultilevel()) {
                continue;
            }

            additional.add(suggestion.slashLevel(handleMulti ? 1 : 0));
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

    public boolean isFailed() {
        return failed;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public static FindResult none(LiteInvocation invocation) {
        return new FindResult(invocation, new ArrayList<>(), null, new ArrayList<>(), new ArrayList<>(), null, null, false, false, false);
    }

}
