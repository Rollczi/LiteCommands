package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.util.MapUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class ParserSetImpl<SENDER, PARSED> implements ParserSet<SENDER, PARSED> {

    private final Class<PARSED> parsedType;
    private final Map<Class<?>, Parser<SENDER, ?, PARSED>> parsers = new HashMap<>();
    private final ParserSet<SENDER, PARSED> parent;

    public ParserSetImpl(Class<PARSED> parsedType) {
        this.parsedType = parsedType;
        this.parent = new EmptyParserSetImpl();
    }

    public ParserSetImpl(Class<PARSED> parsedType, ParserSet<SENDER, PARSED> parent) {
        this.parsedType = parsedType;
        this.parent = parent;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <INPUT> Optional<Parser<SENDER, INPUT, PARSED>> getParser(Class<INPUT> inType) {
        Optional<Parser<SENDER, INPUT, PARSED>> parserOptional = MapUtil.findBySuperTypeOf(inType, this.parsers)
            .map(senderparsedArgumentParser -> (Parser<SENDER, INPUT, PARSED>) senderparsedArgumentParser);

        if (parserOptional.isPresent()) {
            return parserOptional;
        }

        return parent.getParser(inType);
    }

    @Override
    public Collection<Parser<SENDER, ?, PARSED>> getParsers() {
        ArrayList<Parser<SENDER, ?, PARSED>> list = new ArrayList<>(parsers.values());

        list.addAll(parent.getParsers());

        return Collections.unmodifiableList(list);
    }

    public Class<PARSED> getParsedType() {
        return parsedType;
    }

    void registerParser(Parser<SENDER, ?, PARSED> parser) {
        parsers.put(parser.getInputType(), parser);
    }

}
