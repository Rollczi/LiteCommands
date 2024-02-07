package dev.rollczi.litecommands.adventure;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import net.kyori.adventure.text.Component;

class AdventureComponentArgument<SENDER> implements Parser<SENDER, Component> {

    @Override
    public ParseResult<Component> parse(Invocation<SENDER> invocation, Argument<Component> argument, RawInput input) {
        return ParseResult.success(Component.text(input.next()));
    }

    @Override
    public Range getRange(Argument<Component> componentArgument) {
        return Range.ONE;
    }

}
