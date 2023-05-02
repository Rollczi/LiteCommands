package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.ArgumentKey;

public interface ArgumentParserRegistry<SENDER> {

    <PARSED> void registerParser(Class<PARSED> parserType, ArgumentKey key, ArgumentParser<SENDER, ?, PARSED> parser);

    <PARSED> ArgumentParserSet<SENDER, PARSED> getParserSet(Class<PARSED> parsedClass, ArgumentKey key);

    static <SENDER> ArgumentParserRegistry<SENDER> createRegistry() {
        return new ArgumentParserRegistryImpl<>();
    }

}
