
package dev.rollczi.litecommands.argument.resolver.collector;

import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import java.util.TreeSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class TreeSetArgumentResolver<SENDER> extends AbstractCollectorArgumentResolver<SENDER, Object, TreeSet> {

    public TreeSetArgumentResolver(ParserRegistry<SENDER> parserRegistry, SuggesterRegistry<SENDER> suggesterRegistry) {
        super(parserRegistry, suggesterRegistry);
    }

    @Override
    Collector<Object, ?, TreeSet<Object>> getCollector(CollectorArgument<TreeSet> collectorArgument, Invocation<SENDER> invocation) {
        return Collectors.toCollection(() -> new TreeSet<>());
    }

    @Override
    protected Class<Object> getElementType(CollectorArgument<TreeSet> context, Invocation<SENDER> invocation) {
        TypeToken<?> elementType =  context.getWrapperFormat().parsedType().getParameterized();

        return (Class<Object>) elementType.getRawType();
    }

}

