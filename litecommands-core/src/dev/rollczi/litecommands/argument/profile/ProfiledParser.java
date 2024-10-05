package dev.rollczi.litecommands.argument.profile;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.range.Range;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public abstract class ProfiledParser<SENDER, T, PROFILE> implements Parser<SENDER, T> {

    private final ArgumentProfileNamespace<PROFILE> namespace;

    protected ProfiledParser(ArgumentProfileNamespace<PROFILE> namespace) {
        this.namespace = namespace;
    }

    @Override
    public final boolean canParse(Argument<T> argument) {
        Meta meta = argument.meta();
        boolean hasMeta = meta.has(namespace.asMetaKey());

        if (!hasMeta) {
            return false;
        }

        PROFILE value = meta.get(namespace.asMetaKey());
        return this.canParse(argument, value);
    }

    protected boolean canParse(Argument<T> argument, PROFILE profile) {
        return Parser.super.canParse(argument);
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

    protected abstract Range getRange(Argument<T> argument, PROFILE meta);

    @Override
    public final boolean match(Invocation<SENDER> invocation, Argument<T> argument, RawInput input) {
        Meta meta = argument.meta();
        PROFILE value = meta.get(namespace.asMetaKey());

        return this.match(invocation, argument, input, value);
    }

    protected boolean match(Invocation<SENDER> invocation, Argument<T> argument, RawInput input, PROFILE profile) {
        return Parser.super.match(invocation, argument, input);
    }

}
