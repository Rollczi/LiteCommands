package dev.rollczi.litecommands.argument.resolver.collector;

import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import java.util.Vector;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class VectorArgumentResolver<SENDER> extends AbstractCollectorArgumentResolver<SENDER, Object, Vector> {

    public VectorArgumentResolver(ParserRegistry<SENDER> parserRegistry, SuggesterRegistry<SENDER> suggesterRegistry) {
        super(parserRegistry, suggesterRegistry);
    }

    @Override
    Collector<Object, ?, Vector<Object>> getCollector(CollectorArgument<Vector> collectorArgument, Invocation<SENDER> invocation) {
        return Collectors.toCollection(() -> new Vector<>());
    }

    @Override
    protected Class<Object> getElementType(CollectorArgument<Vector> context, Invocation<SENDER> invocation) {
        TypeToken<?> elementType =  context.getWrapperFormat().parsedType().getParameterized();

        return (Class<Object>) elementType.getRawType();
    }

}

