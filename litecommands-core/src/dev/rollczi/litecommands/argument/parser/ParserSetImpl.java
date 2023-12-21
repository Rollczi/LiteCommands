package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.reflect.type.TypeIndex;

import dev.rollczi.litecommands.reflect.type.TypeRange;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.ApiStatus;

class ParserSetImpl<SENDER, PARSED> implements ParserSet<SENDER, PARSED> {

    private final TypeRange<PARSED> parsedType;
    private final TypeIndex<Parser<SENDER, ?, PARSED>> parsers = new TypeIndex<>();

    public ParserSetImpl(TypeRange<PARSED> parsedType) {
        this.parsedType = parsedType;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <INPUT> List<Parser<SENDER, INPUT, PARSED>> getParsers(Class<INPUT> inType) {
        return this.parsers.get(inType).stream()
            .map(parser -> (Parser<SENDER, INPUT, PARSED>) parser)
            .collect(Collectors.toList());
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "3.3.0")
    public Class<PARSED> getParsedType() {
        return parsedType.getBaseType();
    }

    void registerParser(Parser<SENDER, ?, PARSED> parser) {
        parsers.put(TypeRange.upwards(parser.getInputType()), parser);
    }

}
