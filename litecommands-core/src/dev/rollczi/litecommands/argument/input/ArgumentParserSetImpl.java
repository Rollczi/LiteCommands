package dev.rollczi.litecommands.argument.input;

import dev.rollczi.litecommands.util.MapUtil;
import panda.std.Option;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class ArgumentParserSetImpl<SENDER, PARSED> implements ArgumentParserSet<SENDER, PARSED> {

    private final Class<PARSED> parsedType;
    private final Map<Class<?>, ArgumentParser<SENDER, ?, PARSED>> parsers = new HashMap<>();
    private final ArgumentParserSet<SENDER, PARSED> parent;

    public ArgumentParserSetImpl(Class<PARSED> parsedType) {
        this.parsedType = parsedType;
        this.parent = new EmptyArgumentParserSetImpl();
    }

    public ArgumentParserSetImpl(Class<PARSED> parsedType, ArgumentParserSet<SENDER, PARSED> parent) {
        this.parsedType = parsedType;
        this.parent = parent;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <INPUT> Optional<ArgumentParser<SENDER, INPUT, PARSED>> getParser(Class<INPUT> inType) {
        return MapUtil.findKeySuperTypeOf(inType, this.parsers)
            .map(senderparsedArgumentParser -> (ArgumentParser<SENDER, INPUT, PARSED>) senderparsedArgumentParser)
            .orElse(Option.ofOptional(parent.getParser(inType)))
            .toOptional();
    }

    public Class<PARSED> getParsedType() {
        return parsedType;
    }

    void registerParser(ArgumentParser<SENDER, ?, PARSED> parser) {
        parsers.put(parser.getInputType(), parser);
    }

    private class EmptyArgumentParserSetImpl implements ArgumentParserSet<SENDER,PARSED> {
        @Override
        public <INPUT> Optional<ArgumentParser<SENDER, INPUT, PARSED>> getParser(Class<INPUT> inType) {
            return Optional.empty();
        }

        public Class<PARSED> getParsedType() {
            throw new UnsupportedOperationException();
        }
    }
}
