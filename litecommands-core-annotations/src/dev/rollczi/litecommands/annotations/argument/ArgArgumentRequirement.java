package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotations.command.ParameterCommandRequirement;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.argument.FailedReason;
import dev.rollczi.litecommands.argument.parser.ArgumentParserSet;
import dev.rollczi.litecommands.command.requirements.CommandArgumentRequirement;
import dev.rollczi.litecommands.command.requirements.CommandRequirementResult;
import dev.rollczi.litecommands.argument.SuccessfulResult;
import dev.rollczi.litecommands.argument.input.InputArguments;
import dev.rollczi.litecommands.argument.input.InputArgumentsMatcher;
import dev.rollczi.litecommands.invalid.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.wrapper.Wrapper;
import dev.rollczi.litecommands.wrapper.WrapFormat;

import java.lang.reflect.Parameter;

class ArgArgumentRequirement<SENDER, PARSED> implements ParameterCommandRequirement<SENDER, PARSED>, CommandArgumentRequirement<SENDER, PARSED> {

    private final ParameterArgument<?, PARSED> argument;
    private final Wrapper wrapper;
    private final ArgumentParserSet<SENDER, PARSED> parserSet;

    ArgArgumentRequirement(ParameterArgument<?, PARSED> argument, Wrapper wrapper, ArgumentParserSet<SENDER, PARSED> parserSet) {
        this.argument = argument;
        this.wrapper = wrapper;
        this.parserSet = parserSet;
    }

    @Override
    public <MATCHER extends InputArgumentsMatcher<MATCHER>> CommandRequirementResult<PARSED> check(Invocation<SENDER> invocation, MATCHER matcher) {
        ArgumentResult<PARSED> result = matcher.nextArgument(invocation, argument, parserSet);

        if (result.isSuccessful()) {
            SuccessfulResult<PARSED> successfulResult = result.getSuccessfulResult();

            return CommandRequirementResult.success(() -> wrapper.create(successfulResult.getExpectedProvider(), this.getWrapperFormat()));
        }

        FailedReason failedReason = result.getFailedReason();

        if (failedReason.getReason() == InvalidUsage.Cause.MISSING_ARGUMENT && wrapper.canCreateEmpty()) {
            return CommandRequirementResult.success(() -> wrapper.createEmpty(this.getWrapperFormat()));
        }

        return CommandRequirementResult.failure(failedReason);
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
