package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.reflect.type.TypeRange;

public interface ParserRegistry<SENDER> {

    @Deprecated
    default <PARSED> void registerParser(Class<PARSED> parserType, ArgumentKey key, Parser<SENDER, PARSED> parser) {
        registerParser(TypeRange.downwards(parserType), key, parser);
    }

    <PARSED> void registerParser(TypeRange<PARSED> typeRange, ArgumentKey key, Parser<SENDER, PARSED> parser);

    <PARSED> ParserSet<SENDER, PARSED> getParserSet(Class<PARSED> parsedClass, ArgumentKey key);

}
