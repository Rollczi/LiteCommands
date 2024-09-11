package dev.rollczi.litecommands.command.executor;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.MultipleArgumentResolver;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.priority.PriorityLevel;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.requirement.BindRequirement;
import dev.rollczi.litecommands.requirement.ContextRequirement;
import dev.rollczi.litecommands.requirement.RequirementsResult;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public class CommandExecutorDefaultReference<SENDER> implements CommandExecutor<SENDER> {

    public static final Argument<Void> DEFAULT_ARGUMENT = Argument.of("default", WrapFormat.notWrapped(Void.class));
    public static final MultipleArgumentResolver<?, Void> DEFAULT_ARGUMENT_RESOLVER = new DefaultArgumentResolver<>();

    private final CommandExecutor<SENDER> executor;

    public CommandExecutorDefaultReference(
        CommandExecutor<SENDER> executor
    ) {
        if (!executor.getArguments().isEmpty()) {
            throw new IllegalArgumentException("Default executor cannot have any arguments");
        }

        this.executor = executor;
        this.executor.meta().put(Meta.IGNORE_TOO_MANY_ARGUMENTS, true);
    }

    @Override
    public @Unmodifiable List<Argument<?>> getArguments() {
        return Collections.singletonList(DEFAULT_ARGUMENT);
    }

    @Override
    public @Unmodifiable List<ContextRequirement<?>> getContextRequirements() {
        return executor.getContextRequirements();
    }

    @Override
    public @Unmodifiable List<BindRequirement<?>> getBindRequirements() {
        return executor.getBindRequirements();
    }

    @Override
    public CommandExecutorMatchResult match(RequirementsResult<SENDER> result) {
        return executor.match(result);
    }

    @Override
    public CommandRoute<SENDER> getParent() {
        return executor.getParent();
    }

    @Override
    public Meta meta() {
        return executor.meta();
    }

    @Override
    public @Nullable MetaHolder parentMeta() {
        return executor.parentMeta();
    }

    @Override
    public PriorityLevel getPriority() {
        return PriorityLevel.NONE;
    }

    private static class DefaultArgumentResolver<SENDER> implements MultipleArgumentResolver<SENDER, Void> {

        @Override
        public ParseResult<Void> parse(Invocation<SENDER> invocation, Argument<Void> argument, RawInput rawInput) {
            while (rawInput.hasNext()) {
                rawInput.next();
            }

            return ParseResult.successNull();
        }

        @Override
        public Range getRange(Argument<Void> voidArgument) {
            return Range.moreThan(0);
        }

    }

}
