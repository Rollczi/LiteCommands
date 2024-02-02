package dev.rollczi.litecommands.command.executor;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.MultipleArgumentResolver;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.priority.PriorityLevel;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.requirement.BindRequirement;
import dev.rollczi.litecommands.requirement.ContextRequirement;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractDefaultCommandExecutor<SENDER> extends AbstractCommandExecutor<SENDER> implements CommandExecutor<SENDER> {

    public static final Argument<Void> DEFAULT_ARGUMENT = Argument.of("default", WrapFormat.notWrapped(Void.class));
    public static final MultipleArgumentResolver<?, Void> DEFAULT_ARGUMENT_RESOLVER = new DefaultArgumentResolver<>();

    protected AbstractDefaultCommandExecutor(
        CommandRoute<SENDER> parent,
        Collection<Argument<?>> arguments,
        Collection<ContextRequirement<?>> contextRequirements,
        Collection<BindRequirement<?>> bindRequirements
    ) {
        super(parent, appendDefaultArgument(arguments), contextRequirements, bindRequirements);
    }

    private static Collection<Argument<?>> appendDefaultArgument(Collection<Argument<?>> arguments) {
        ArrayList<Argument<?>> arrayList = new ArrayList<>(arguments);
        arrayList.add(DEFAULT_ARGUMENT);

        return arrayList;
    }

    @Override
    public PriorityLevel getPriority()  {
        return PriorityLevel.NONE;
    }

    private static class DefaultArgumentResolver<SENDER> implements MultipleArgumentResolver<SENDER, Void> {

        @Override
        public ParseResult<Void> parse(Invocation<SENDER> invocation, Argument<Void> argument, RawInput rawInput) {
            while (rawInput.hasNext()) {
                rawInput.next();
            }

            return ParseResult.success(null);
        }

        @Override
        public Range getRange(Argument<Void> voidArgument) {
            return Range.moreThan(0);
        }

    }

}
