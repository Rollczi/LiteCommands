package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentState;
import dev.rollczi.litecommands.command.execute.ArgumentExecutor;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.shared.Validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class FindResult {

    private final LiteInvocation invocation;

    private final List<CommandSection> sections;
    private final ArgumentExecutor executor;
    private final List<ArgumentState> argumentStates;
    private final List<CompletedArgument> arguments;

    private final CompletedArgument lastMatchedArgument;
    private final CompletedArgument failedArgument;

    private final boolean found;
    private final boolean failed;
    private final boolean invalid;
    private final Object invalidResult;

    private FindResult(LiteInvocation invocation, List<CommandSection> sections, ArgumentExecutor executor, List<ArgumentState> argumentStates, List<CompletedArgument> arguments, CompletedArgument lastMatchedArgument, CompletedArgument failedArgument, boolean found, boolean failed, boolean invalid, Object invalidResult) {
        this.invocation = invocation;
        this.sections = sections;
        this.executor = executor;
        this.argumentStates = argumentStates;
        this.arguments = arguments;
        this.lastMatchedArgument = lastMatchedArgument;
        this.failedArgument = failedArgument;
        this.found = found;
        this.failed = failed;
        this.invalid = invalid;
        this.invalidResult = invalidResult;
    }

    public FindResult withSection(CommandSection section) {
        Validation.isNull(this.executor, "Executor is set");

        List<CommandSection> sections = new ArrayList<>(this.sections);

        sections.add(section);
        return new FindResult(invocation, sections, null, argumentStates, arguments, lastMatchedArgument, failedArgument, found, failed, invalid, invalidResult);
    }

    public FindResult withExecutor(ArgumentExecutor executor, List<ArgumentState> argumentStates) {
        Validation.isNull(this.executor, "Executor already set");
        Validation.isFalse(this.found, "This is the end of the command");
        Validation.isNotEmpty(this.sections, "Executor must have a parent section");

        if (sections.isEmpty()) {
            throw new IllegalStateException();
        }

        for (ArgumentExecutor exec : sections.get(sections.size() - 1).executors()) {
            if (exec.equals(executor)) {
                return new FindResult(invocation, sections, executor, argumentStates, arguments, lastMatchedArgument, failedArgument, false, failed, invalid, invalidResult);
            }
        }

        throw new IllegalArgumentException("Executor not found in last section.");
    }

    public FindResult withArgument(Argument<?> argument, List<Object> results, List<Suggestion> suggestions, boolean matched) {
        Validation.isNotNull(this.executor, "Executor not set");
        Validation.isFalse(this.found, "FindResult is found");
        Validation.isFalse(this.failed, "FindResult is failed");
        Validation.isFalse(this.invalid, "FindResult is invalid");

        List<CompletedArgument> arguments = new ArrayList<>(this.arguments);
        CompletedArgument completedArgument = new CompletedArgument(argument, results, suggestions, arguments.size());

        arguments.add(completedArgument);
        return new FindResult(invocation, sections, executor, argumentStates, arguments, matched ? completedArgument : this.lastMatchedArgument, failedArgument, false, failed, invalid, invalidResult);
    }

    public FindResult failedArgument(Argument<?> argument, List<Suggestion> suggestions) {
        Validation.isNotNull(this.executor, "Executor not set");
        Validation.isFalse(this.found, "FindResult is found");
        Validation.isFalse(this.failed, "FindResult is failed");
        Validation.isFalse(this.invalid, "FindResult is invalid");

        CompletedArgument completedArgument = new CompletedArgument(argument, Collections.emptyList(), suggestions, arguments.size());

        return new FindResult(invocation, sections, executor, argumentStates, arguments, lastMatchedArgument, completedArgument, false, true, false, invalidResult);
    }

    public FindResult invalidArgument(Argument<?> argument, List<Suggestion> suggestions, Object result) {
        Validation.isNotNull(result, "result is null");
        Validation.isNotNull(this.executor, "Executor not set");
        Validation.isFalse(this.found, "FindResult is found");
        Validation.isFalse(this.failed, "FindResult is failed");
        Validation.isFalse(this.invalid, "FindResult is invalid");

        CompletedArgument completedArgument = new CompletedArgument(argument, Collections.emptyList(), suggestions, arguments.size());

        return new FindResult(invocation, sections, executor, argumentStates, arguments, lastMatchedArgument, completedArgument, false, false, true, result);
    }

    public FindResult invalid() {
        Validation.isNotNull(this.executor, "Executor not set");
        Validation.isFalse(this.found, "FindResult is found");
        Validation.isFalse(this.failed, "FindResult is failed");
        Validation.isFalse(this.invalid, "FindResult is invalid");

        return new FindResult(invocation, sections, executor, argumentStates, arguments, lastMatchedArgument, failedArgument, false, false, true, invalidResult);
    }

    public FindResult markAsFound() {
        Validation.isFalse(this.found, "FindResult is found");
        Validation.isFalse(this.failed, "FindResult is failed");
        Validation.isFalse(this.invalid, "FindResult is invalid");

        return new FindResult(invocation, sections, executor, argumentStates, arguments, lastMatchedArgument, failedArgument, true, false, false, invalidResult);
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

    public List<ArgumentState> getArgumentStates() {
        return Collections.unmodifiableList(argumentStates);
    }

    public List<Argument<?>> getArguments() {
        return this.arguments.stream()
                .map(completedArgument -> completedArgument.argument)
                .collect(Collectors.toList());
    }

    public Optional<Object> getInvalidResult() {
        return Optional.ofNullable(this.invalidResult);
    }

    public List<Object> extractResults() {
        Validation.isTrue(this.found, "FindResult must be found");

        List<Object> results = new ArrayList<>();

        for (CompletedArgument argument : arguments) {
            results.addAll(argument.results);
        }

        return Collections.unmodifiableList(results);
    }

    public List<Suggestion> extractSuggestion() {
        List<Suggestion> suggestions = extractSuggestion0();
        List<Suggestion> finalSuggestions = new ArrayList<>();
        String last = invocation.lastArgument().orElse(invocation.label()).toLowerCase();

        for (Suggestion suggestion : suggestions) {
            if (!suggestion.asStringFirstPart().startsWith(last)) {
                continue;
            }

            finalSuggestions.add(suggestion);
        }

        return finalSuggestions;
    }

    private List<Suggestion> extractSuggestion0() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Suggestion> suggestions = new ArrayList<>();

        if (!this.getArguments().isEmpty()) {
            if (lastMatchedArgument == null) {
                CommandSection commandSection = sections.get(sections.size() - 1);

                suggestions.add(Suggestion.of(commandSection.getName()));
                suggestions.addAll(Suggestion.of(commandSection.getAliases()));
            }

            int lastStableIndex = lastMatchedArgument == null ? 0 : lastMatchedArgument.index;

            for (int index = 0; index < this.arguments.size(); index++) {
                if (index < lastStableIndex) {
                    continue;
                }

                CompletedArgument completedArgument = this.arguments.get(index);
                suggestions.addAll(completedArgument.suggestions);
            }

            if (this.failedArgument != null) {
                suggestions.addAll(failedArgument.suggestions);
            }

            return suggestions;
        }

        int preSectionIndex = Math.min(sections.size() - 1, invocation.arguments().length - 1);

        if (preSectionIndex == - 1) {
            suggestions.addAll(Suggestion.of(sections.get(0).getCompletable()));
        }
        else {
            CommandSection commandSection = sections.get(preSectionIndex);

            for (CommandSection child : commandSection.childrenSection()) {
                suggestions.addAll(Suggestion.of(child.getCompletable()));
            }
        }

        return suggestions;
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
        return new FindResult(invocation, new ArrayList<>(), null, new ArrayList<>(), new ArrayList<>(), null, null, false, false, false, null);
    }

    @Deprecated
    private final static class CompletedArgument {
        private final Argument<?> argument;
        private final List<Object> results;
        private final List<Suggestion> suggestions;
        private final int index;

        private CompletedArgument(Argument<?> argument, List<Object> results, List<Suggestion> suggestions, int index) {
            this.argument = argument;
            this.results = results;
            this.suggestions = Collections.unmodifiableList(new ArrayList<>(suggestions));
            this.index = index;
        }
    }

}
