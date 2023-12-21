package dev.rollczi.litecommands.argument.parser;

import java.util.List;
import java.util.stream.Collectors;

class MergedParserSetImpl<SENDER, PARSED> implements ParserSet<SENDER, PARSED> {

    private final List<ParserSet<SENDER, PARSED>> parserSets;

    public MergedParserSetImpl(List<ParserSet<SENDER, PARSED>> parserSets) {
        this.parserSets = parserSets;
    }

    @Override
    public <INPUT> List<Parser<SENDER, INPUT, PARSED>> getParsers(Class<INPUT> inType) {
        return parserSets.stream()
            .map(parserSet -> parserSet.getParsers(inType))
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }

}
