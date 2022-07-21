package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.argument.AnnotatedParameter;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.AnnotatedParameterState;
import dev.rollczi.litecommands.command.execute.ArgumentExecutor;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.scheme.SchematicContext;
import dev.rollczi.litecommands.shared.Validation;
import panda.std.Option;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Deprecated
public final class FindResult<SENDER> implements SchematicContext<SENDER> {

    private final Invocation<SENDER> invocation;

    private final List<CommandSection<SENDER>> sections;
    private final ArgumentExecutor<SENDER> executor;
    private final List<AnnotatedParameterState<SENDER, ?>> arguments;

    private final boolean found;
    private final boolean invalid;
    private final Object result;

    private FindResult(
            Invocation<SENDER> invocation,
            List<CommandSection<SENDER>> sections,
            ArgumentExecutor<SENDER> executor,
            List<AnnotatedParameterState<SENDER, ?>> arguments,
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

    public FindResult<SENDER> withSection(CommandSection<SENDER> section) {
        Validation.isNull(this.executor, "Executor is set");

        List<CommandSection<SENDER>> sections = new ArrayList<>(this.sections);

        sections.add(section);
        return new FindResult<>(invocation, sections, null, arguments, found, invalid, result);
    }

    public FindResult<SENDER> withExecutor(ArgumentExecutor<SENDER> executor) {
        Validation.isNull(this.executor, "Executor already set");
        Validation.isFalse(this.found, "This is the end of the command");
        Validation.isNotEmpty(this.sections, "Executor must have a parent section");

        if (sections.isEmpty()) {
            throw new IllegalStateException();
        }

        for (ArgumentExecutor<SENDER> exec : sections.get(sections.size() - 1).executors()) {
            if (exec.equals(executor)) {
                return new FindResult<>(invocation, sections, executor, arguments, false, invalid, result);
            }
        }

        throw new IllegalArgumentException("Executor not found in last section.");
    }

    public FindResult<SENDER> withArgument(AnnotatedParameterState<SENDER, ?> state) {
        Validation.isNotNull(this.executor, "Executor not set");
        Validation.isFalse(this.found, "FindResult is found");
        Validation.isFalse(this.invalid, "FindResult is invalid");

        List<AnnotatedParameterState<SENDER, ?>> arguments = new ArrayList<>(this.arguments);

        arguments.add(state);
        return new FindResult<>(invocation, sections, executor, arguments, false, false, result);
    }

    public FindResult<SENDER> found() {
        Validation.isFalse(this.found, "FindResult is found");
        Validation.isFalse(this.invalid, "FindResult is invalid");

        return new FindResult<>(invocation, sections, executor, arguments, true, false, result);
    }

    public FindResult<SENDER> found(Object result) {
        Validation.isFalse(this.found, "FindResult is found");
        Validation.isFalse(this.invalid, "FindResult is invalid");

        return new FindResult<>(invocation, sections, executor, arguments, true, false, result);
    }

    public FindResult<SENDER> invalid() {
        Validation.isFalse(this.found, "FindResult is found");
        Validation.isFalse(this.invalid, "FindResult is invalid");

        return new FindResult<>(invocation, sections, executor, arguments, false, true, result);
    }

    public FindResult<SENDER> invalid(Object result) {
        Validation.isFalse(this.found, "FindResult is found");
        Validation.isFalse(this.invalid, "FindResult is invalid");

        return new FindResult<>(invocation, sections, executor, arguments, false, true, result);
    }

    public FindResult<SENDER> failed() {
        Validation.isFalse(this.found, "FindResult is found");
        Validation.isFalse(this.invalid, "FindResult is invalid");

        return new FindResult<>(invocation, sections, executor, arguments, false, false, result);
    }

    @Override
    public Invocation<SENDER> getInvocation() {
        return this.invocation;
    }

    @Override
    public List<CommandSection<SENDER>> getSections() {
        return Collections.unmodifiableList(sections);
    }

    @Override
    public Optional<ArgumentExecutor<SENDER>> getExecutor() {
        return Optional.ofNullable(executor);
    }

    @Override
    public List<AnnotatedParameter<SENDER, ?>> getAllArguments() {
        return Collections.unmodifiableList(executor.annotatedParameters());
    }

    public List<Argument<SENDER, ?>> getArguments() {
        return this.arguments.stream()
                .map(AnnotatedParameter::argument)
                .collect(Collectors.toList());
    }

    public Option<Object> getResult() {
        return Option.of(result);
    }

    public List<Object> extractResults() {
        Validation.isTrue(this.found, "FindResult must be found");

        List<Object> results = new ArrayList<>();

        for (AnnotatedParameterState<SENDER, ?> state : arguments) {
            MatchResult matchResult = state.matchResult();

            if (matchResult.isNotMatched()) {
                if (!state.argument().isOptional()) {
                    throw new IllegalStateException("Can't extract result from not matched FindResult");
                }

                results.addAll(state.argument().defaultValue());
            }

            results.addAll(state.result());
        }

        return Collections.unmodifiableList(results);
    }

    public boolean isLongerThan(FindResult<SENDER> findResult) {
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

    public static <T> FindResult<T> none(Invocation<T> invocation) {
        return new FindResult<>(invocation, new ArrayList<>(), null,  new ArrayList<>(), false, false, null);
    }

}
