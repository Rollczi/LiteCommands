package dev.rollczi.litecommands.modern.command.argument.invocation;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;

public class ArgumentResolverImpl<SENDER, DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> implements ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT> {

    private final ArgumentInvocationParserInternal<SENDER, DETERMINANT, EXPECTED, CONTEXT> parser;

    public ArgumentResolverImpl(ArgumentInvocationParserInternal<SENDER, DETERMINANT, EXPECTED, CONTEXT> parser) {
        this.parser = parser;
    }

    @Override
    public ArgumentResultCollector<SENDER> resolve(CONTEXT context, ArgumentResultPreparedCollector<SENDER, DETERMINANT, EXPECTED, CONTEXT> collector) {
        return collector.collect(this.parser.getArgumentsRange().getMin(), parser);
    }
}
