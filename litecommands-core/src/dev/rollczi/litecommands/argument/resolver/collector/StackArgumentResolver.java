package dev.rollczi.litecommands.argument.resolver.collector;

import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import java.util.Stack;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class StackArgumentResolver<SENDER> extends AbstractCollectorArgumentResolver<SENDER, Object, Stack> {

    public StackArgumentResolver(ParserRegistry<SENDER> parserRegistry, SuggesterRegistry<SENDER> suggesterRegistry) {
        super(parserRegistry, suggesterRegistry);
    }

    @Override
    Collector<Object, ?, Stack<Object>> getCollector(CollectorArgument<Stack> collectorArgument, Invocation<SENDER> invocation) {
        return Collectors.toCollection(() -> new Stack<>());
    }

    @Override
    protected Class<Object> getElementType(CollectorArgument<Stack> context, Invocation<SENDER> invocation) {
        TypeToken<?> elementType =  context.getWrapperFormat().parsedType().getParameterized();

        return (Class<Object>) elementType.getRawType();
    }

}

