package dev.rollczi.litecommands.argument.resolver.collector;

import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SetArgumentResolver<SENDER> extends AbstractCollectorArgumentResolver<SENDER, Object, Set> {

    public SetArgumentResolver(ParserRegistry<SENDER> parserRegistry, SuggesterRegistry<SENDER> suggesterRegistry) {
        super(parserRegistry, suggesterRegistry);
    }

    @Override
    Collector<Object, ?, Set<Object>> getCollector(CollectorArgument<Set> collectorArgument, Invocation<SENDER> invocation) {
        return Collectors.toSet();
    }

    @Override
    protected Class<Object> getElementType(CollectorArgument<Set> context, Invocation<SENDER> invocation) {
        TypeToken<?> elementType =  context.getWrapperFormat().parsedType().getParameterized();

        return (Class<Object>) elementType.getRawType();
    }

}

