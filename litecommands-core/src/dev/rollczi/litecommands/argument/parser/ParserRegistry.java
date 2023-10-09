package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.ArgumentKey;

public interface ParserRegistry<SENDER> {

    <PARSED> void registerParser(Class<PARSED> parserType, ArgumentKey key, Parser<SENDER, ?, PARSED> parser);

    <PARSED> ParserSet<SENDER, PARSED> getParserSet(Class<PARSED> parsedClass, ArgumentKey key);

}
