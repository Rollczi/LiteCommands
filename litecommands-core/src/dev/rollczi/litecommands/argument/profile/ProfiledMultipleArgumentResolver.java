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
public abstract class ProfiledMultipleArgumentResolver<SENDER, T, PROFILE> implements MultipleArgumentResolver<SENDER, T> {

    private final ArgumentProfileNamespace<PROFILE> namespace;

    protected ProfiledMultipleArgumentResolver(ArgumentProfileNamespace<PROFILE> namespace) {
        this.namespace = namespace;
    }

    @Override
    public final boolean canParse(Argument<T> argument) {
        Meta meta = argument.meta();
        boolean hasMeta = meta.has(namespace.asMetaKey());

        if (!hasMeta) {
            return false;
        }

        PROFILE profile = meta.get(namespace.asMetaKey());
        return this.canParse(argument, profile);
    }

    protected boolean canParse(Argument<T> argument, PROFILE profile) {
        return MultipleArgumentResolver.super.canParse(argument);
    }

    @Override
    public final ParseResult<T> parse(Invocation<SENDER> invocation, Argument<T> argument, RawInput input) {
        Meta meta = argument.meta();
        PROFILE value = meta.get(namespace.asMetaKey());

        return this.parse(invocation, argument, input, value);
    }

    protected abstract ParseResult<T> parse(Invocation<SENDER> invocation, Argument<T> argument, RawInput rawInput, PROFILE profile);

    @Override
    public final Range getRange(Argument<T> argument) {
        return this.getRange(argument, argument.meta().get(namespace.asMetaKey()));
    }

    protected abstract Range getRange(Argument<T> argument, PROFILE profile);

    @Override
    public final SuggestionResult suggest(Invocation<SENDER> invocation, Argument<T> argument, SuggestionContext context) {
        Meta meta = argument.meta();
        PROFILE value = meta.get(namespace.asMetaKey());

        return this.suggest(invocation, argument, context, value);
    }

    protected SuggestionResult suggest(Invocation<SENDER> invocation, Argument<T> argument, SuggestionContext context, PROFILE profile) {
        return MultipleArgumentResolver.super.suggest(invocation, argument, context);
    }

    @Override
    public final boolean match(Invocation<SENDER> invocation, Argument<T> argument, RawInput input) {
        Meta meta = argument.meta();
        PROFILE value = meta.get(namespace.asMetaKey());

        return this.match(invocation, argument, input, value);
    }

    protected boolean match(Invocation<SENDER> invocation, Argument<T> argument, RawInput input, PROFILE profile) {
        return MultipleArgumentResolver.super.match(invocation, argument, input);
    }

}
