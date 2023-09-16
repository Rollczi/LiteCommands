package dev.rollczi.litecommands.requirement;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.ParserSet;
import dev.rollczi.litecommands.argument.parser.input.ParseableInputMatcher;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.annotation.AnnotationHolder;
import dev.rollczi.litecommands.shared.FailedReason;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import dev.rollczi.litecommands.wrapper.Wrapper;
import org.jetbrains.annotations.Nullable;

class ArgumentRequirementImpl<SENDER, PARSED> implements ArgumentRequirement<SENDER, PARSED> {

    private final Argument<PARSED> argument;
    private final Wrapper wrapper;
    private final ParserSet<SENDER, PARSED> parserSet;
    private final Meta meta = Meta.create();

    public ArgumentRequirementImpl(Argument<PARSED> argument, Wrapper wrapper, ParserSet<SENDER, PARSED> parserSet) {
        this.argument = argument;
        this.wrapper = wrapper;
        this.parserSet = parserSet;
    }

    @Override
    public String getName() {
        return argument.getAnnotationHolder().getName();
    }

    @Override
    public AnnotationHolder<?, PARSED, ?> getAnnotationHolder() {
        return argument.getAnnotationHolder();
    }

    @Override
    public <MATCHER extends ParseableInputMatcher<MATCHER>> RequirementResult<PARSED> match(Invocation<SENDER> invocation, MATCHER matcher) {
        ParseResult<PARSED> result = matcher.nextArgument(invocation, argument, parserSet);
        WrapFormat<PARSED, ?> wrapFormat = argument.getAnnotationHolder().getFormat();

        if (result.isSuccessful()) {
            PARSED successfulResult = result.getSuccessfulResult();

            return RequirementResult.success(wrapper.create(successfulResult, wrapFormat));
        }

        FailedReason failedReason = result.getFailedReason();

        if (failedReason.getReason() == InvalidUsage.Cause.MISSING_ARGUMENT && wrapper.canCreateEmpty()) {
            return RequirementResult.success(wrapper.createEmpty(wrapFormat));
        }

        return RequirementResult.failure(failedReason);
    }

    @Override
    public Argument<PARSED> getArgument() {
        return argument;
    }

    @Override
    public boolean isWrapperOptional() {
        return wrapper.canCreateEmpty();
    }

    @Override
    public Meta meta() {
        return this.meta;
    }

    @Override
    public @Nullable MetaHolder parentMeta() {
        return null;
    }

}
