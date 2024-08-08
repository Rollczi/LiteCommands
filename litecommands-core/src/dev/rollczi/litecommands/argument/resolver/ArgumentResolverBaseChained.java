package dev.rollczi.litecommands.argument.resolver;

import dev.rollczi.litecommands.argument.parser.ParserChained;
import dev.rollczi.litecommands.argument.suggester.SuggesterChained;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface ArgumentResolverBaseChained<SENDER, PARSED> extends ParserChained<SENDER, PARSED>, SuggesterChained<SENDER, PARSED> {

}
