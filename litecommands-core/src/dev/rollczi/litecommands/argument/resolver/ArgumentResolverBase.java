package dev.rollczi.litecommands.argument.resolver;

import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.suggester.Suggester;

public interface ArgumentResolverBase<SENDER, INPUT, PARSED> extends Suggester<SENDER, PARSED>, Parser<SENDER, INPUT, PARSED> {
}
