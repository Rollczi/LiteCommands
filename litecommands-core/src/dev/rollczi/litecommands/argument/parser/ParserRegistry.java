package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.reflect.type.TypeRange;

public interface ParserRegistry<SENDER> {

    @Deprecated
    default <PARSED> void registerParser(Class<PARSED> parserType, ArgumentKey key, Parser<SENDER, PARSED> parser) {
        registerParser(TypeRange.downwards(parserType), key, parser);
    }

    <PARSED> void registerParser(TypeRange<PARSED> typeRange, ArgumentKey key, Parser<SENDER, PARSED> parser);

    <PARSED> void registerParser(TypeRange<PARSED> typeRange, ArgumentKey key, ParserChained<SENDER, PARSED> parser);

    <PARSED> ParserSet<SENDER, PARSED> getParserSet(Class<PARSED> parsedClass, ArgumentKey key);

    <PARSED> Parser<SENDER, PARSED> getParser(Invocation<SENDER> invocation, Argument<PARSED> argument);

}
