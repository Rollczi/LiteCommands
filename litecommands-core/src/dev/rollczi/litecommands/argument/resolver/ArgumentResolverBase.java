package dev.rollczi.litecommands.argument.resolver;

import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.suggester.Suggester;

public interface ArgumentResolverBase<SENDER, PARSED> extends Suggester<SENDER, PARSED>, Parser<SENDER, PARSED> {
}
