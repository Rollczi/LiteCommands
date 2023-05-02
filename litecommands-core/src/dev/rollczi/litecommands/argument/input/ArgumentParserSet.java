package dev.rollczi.litecommands.argument.input;

import java.util.Optional;

public interface ArgumentParserSet<SENDER, PARSED> {

    <INPUT> Optional<ArgumentParser<SENDER, INPUT, PARSED>> getParser(Class<INPUT> inType);

}
