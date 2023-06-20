package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotations.command.ParameterRequirement;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.input.ParsableInputMatcher;
import dev.rollczi.litecommands.shared.FailedReason;
import dev.rollczi.litecommands.argument.parser.ParserSet;
import dev.rollczi.litecommands.command.requirement.ArgumentRequirement;
import dev.rollczi.litecommands.command.requirement.RequirementResult;
import dev.rollczi.litecommands.invalid.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.wrapper.ValueToWrap;
import dev.rollczi.litecommands.wrapper.Wrapper;
import dev.rollczi.litecommands.wrapper.WrapFormat;

import java.lang.reflect.Parameter;

class ArgArgumentRequirement<SENDER, PARSED> implements ParameterRequirement<SENDER, PARSED>, ArgumentRequirement<SENDER, PARSED> {

    private final ParameterArgument<?, PARSED> argument;
    private final Wrapper wrapper;
    private final ParserSet<SENDER, PARSED> parserSet;

    ArgArgumentRequirement(ParameterArgument<?, PARSED> argument, Wrapper wrapper, ParserSet<SENDER, PARSED> parserSet) {
        this.argument = argument;
        this.wrapper = wrapper;
        this.parserSet = parserSet;
    }

    @Override
    public <MATCHER extends ParsableInputMatcher<MATCHER>> RequirementResult<PARSED> match(Invocation<SENDER> invocation, MATCHER matcher) {
        ParseResult<PARSED> result = matcher.nextArgument(invocation, argument, parserSet);

        if (result.isSuccessful()) {
            ValueToWrap<PARSED> successfulResult = result.getSuccessfulResult();

            return RequirementResult.success(() -> wrapper.create(successfulResult, this.getWrapperFormat()));
        }

        FailedReason failedReason = result.getFailedReason();

        if (failedReason.getReason() == InvalidUsage.Cause.MISSING_ARGUMENT && wrapper.canCreateEmpty()) {
            return RequirementResult.success(() -> wrapper.createEmpty(this.getWrapperFormat()));
        }

        return RequirementResult.failure(failedReason);
    }

    public WrapFormat<PARSED, ?> getWrapperFormat() {
        return argument.getWrapperFormat();
    }

    public Parameter getParameter() {
        return argument.getParameter();
    }

    public int getParameterIndex() {
        return argument.getParameterIndex();
    }

    @Override
    public Argument<?> getArgument() {
        return argument;
    }

    @Override
    public boolean isOptional() {
        return wrapper.canCreateEmpty();
    }

}
