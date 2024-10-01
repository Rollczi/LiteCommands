package dev.rollczi.litecommands.argument.profile;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.MultipleArgumentResolver;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public abstract class ProfiledMultipleArgumentResolver<SENDER, T, META> implements MultipleArgumentResolver<SENDER, T> {

    private final ArgumentProfileKey<META> key;

    protected ProfiledMultipleArgumentResolver(ArgumentProfileKey<META> key) {
        this.key = key;
    }

    @Override
    public final boolean canParse(Argument<T> argument) {
        Meta meta = argument.meta();
        boolean hasMeta = meta.has(key.asMetaKey());

        if (!hasMeta) {
            return false;
        }

        META value = meta.get(key.asMetaKey());
        return this.canParse(argument, value);
    }

    protected boolean canParse(Argument<T> argument, META value) {
        return MultipleArgumentResolver.super.canParse(argument);
    }

    @Override
    public final ParseResult<T> parse(Invocation<SENDER> invocation, Argument<T> argument, RawInput input) {
        Meta meta = argument.meta();
        META value = meta.get(key.asMetaKey());

        return this.parse(invocation, argument, input, value);
    }

    protected abstract ParseResult<T> parse(Invocation<SENDER> invocation, Argument<T> argument, RawInput rawInput, META meta);

    @Override
    public final Range getRange(Argument<T> argument) {
        return this.getRange(argument, argument.meta().get(key.asMetaKey()));
    }

    protected abstract Range getRange(Argument<T> argument, META meta);

    @Override
    public final SuggestionResult suggest(Invocation<SENDER> invocation, Argument<T> argument, SuggestionContext context) {
        Meta meta = argument.meta();
        META value = meta.get(key.asMetaKey());

        return this.suggest(invocation, argument, context, value);
    }

    protected SuggestionResult suggest(Invocation<SENDER> invocation, Argument<T> argument, SuggestionContext context, META meta) {
        return MultipleArgumentResolver.super.suggest(invocation, argument, context);
    }

    @Override
    public final boolean matchParse(Invocation<SENDER> invocation, Argument<T> argument, RawInput input) {
        Meta meta = argument.meta();
        META value = meta.get(key.asMetaKey());

        return this.matchParse(invocation, argument, input, value);
    }

    protected boolean matchParse(Invocation<SENDER> invocation, Argument<T> argument, RawInput input, META meta) {
        return MultipleArgumentResolver.super.matchParse(invocation, argument, input);
    }

}
