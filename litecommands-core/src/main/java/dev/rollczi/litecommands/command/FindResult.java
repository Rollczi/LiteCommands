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
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class FindResult {

    private final LiteInvocation invocation;

    private final List<CommandSection> sections;
    private final ArgumentExecutor executor;
    private final List<AnnotatedParameterState<?>> arguments;

    private final boolean found;
    private final boolean invalid;
    private final Object result;

    private FindResult(
            LiteInvocation invocation,
            List<CommandSection> sections,
            ArgumentExecutor executor,
            List<AnnotatedParameterState<?>> arguments,
            boolean found,
            boolean invalid,
            Object result
    ) {
        this.invocation = invocation;
        this.sections = sections;
        this.executor = executor;
        this.arguments = arguments;
        this.found = found;
        this.invalid = invalid;
        this.result = result;
    }

    public FindResult withSection(CommandSection section) {
        Validation.isNull(this.executor, "Executor is set");

        List<CommandSection> sections = new ArrayList<>(this.sections);

        sections.add(section);
        return new FindResult(invocation, sections, null, arguments, found, invalid, result);
    }

    public FindResult withExecutor(ArgumentExecutor executor) {
        Validation.isNull(this.executor, "Executor already set");
        Validation.isFalse(this.found, "This is the end of the command");
        Validation.isNotEmpty(this.sections, "Executor must have a parent section");

        if (sections.isEmpty()) {
            throw new IllegalStateException();
        }

        for (ArgumentExecutor exec : sections.get(sections.size() - 1).executors()) {
            if (exec.equals(executor)) {
                return new FindResult(invocation, sections, executor, arguments, false, invalid, result);
            }
        }

        throw new IllegalArgumentException("Executor not found in last section.");
    }

    public FindResult withArgument(AnnotatedParameterState<?> state) {
        Validation.isNotNull(this.executor, "Executor not set");
        Validation.isFalse(this.found, "FindResult is found");
        Validation.isFalse(this.invalid, "FindResult is invalid");

        List<AnnotatedParameterState<?>> arguments = new ArrayList<>(this.arguments);

        arguments.add(state);
        return new FindResult(invocation, sections, executor, arguments, false, false, result);
    }

    public FindResult found() {
        Validation.isFalse(this.found, "FindResult is found");
        Validation.isFalse(this.invalid, "FindResult is invalid");

        return new FindResult(invocation, sections, executor, arguments, true, false, result);
    }

    public FindResult found(Object result) {
        Validation.isFalse(this.found, "FindResult is found");
        Validation.isFalse(this.invalid, "FindResult is invalid");

        return new FindResult(invocation, sections, executor, arguments, true, false, result);
    }

    public FindResult invalid() {
        Validation.isFalse(this.found, "FindResult is found");
        Validation.isFalse(this.invalid, "FindResult is invalid");

        return new FindResult(invocation, sections, executor, arguments, false, true, result);
    }

    public FindResult invalid(Object result) {
        Validation.isFalse(this.found, "FindResult is found");
        Validation.isFalse(this.invalid, "FindResult is invalid");

        return new FindResult(invocation, sections, executor, arguments, false, true, result);
    }

    public FindResult failed() {
        Validation.isFalse(this.found, "FindResult is found");
        Validation.isFalse(this.invalid, "FindResult is invalid");

        return new FindResult(invocation, sections, executor, arguments, false, false, result);
    }

    public LiteInvocation getInvocation() {
        return this.invocation;
    }

    public List<CommandSection> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public Optional<ArgumentExecutor> getExecutor() {
        return Optional.ofNullable(executor);
    }

    public List<AnnotatedParameter<?>> getAllArguments() {
        return Collections.unmodifiableList(executor.annotatedParameters());
    }

    public List<Argument<?>> getArguments() {
        return this.arguments.stream()
                .map(AnnotatedParameter::argument)
                .collect(Collectors.toList());
    }

    public Optional<Object> getResult() {
        return Optional.ofNullable(result);
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
                for (AnnotatedParameter<?> parameter : argumentExecutor.annotatedParameters()) {
                    suggestions.addAll(parameter.toSuggester(invocation).suggestions());

                    if (!parameter.argument().isOptional()) {
                        break;
                    }
                }
            }

            suggesters.add(Suggester.of(suggestions));
        }

        if (executor == null) {
            return known(arguments.iterator(), suggesters.iterator(), Suggester.NONE, Collections.emptyList());
        }

        for (int index = 1; index < executor.annotatedParameters().size(); index++) {
            AnnotatedParameter<?> parameter = executor.annotatedParameters().get(index);
            Suggester suggester = parameter.toSuggester(invocation);

            if (!parameter.argument().isOptional()) {
                suggesters.add(Suggester.of(suggester.suggestions()));
                continue;
            }

            List<Suggestion> suggestions = new ArrayList<>(suggester.suggestions());

            for (int optional = index + 1; optional < executor.annotatedParameters().size(); optional++) {
                AnnotatedParameter<?> annotatedParameter = executor.annotatedParameters().get(optional);
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

    public boolean isFailed() {
        return !found && !invalid;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public static FindResult none(LiteInvocation invocation) {
        return new FindResult(invocation, new ArrayList<>(), null,  new ArrayList<>(), false, false, null);
    }

}
