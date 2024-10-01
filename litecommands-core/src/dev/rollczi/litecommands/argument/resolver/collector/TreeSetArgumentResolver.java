
package dev.rollczi.litecommands.argument.resolver.collector;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.invocation.Invocation;
import java.util.TreeSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class TreeSetArgumentResolver<SENDER> extends AbstractCollectorArgumentResolver<SENDER, TreeSet> {

    public TreeSetArgumentResolver(ParserRegistry<SENDER> parserRegistry, SuggesterRegistry<SENDER> suggesterRegistry) {
        super(parserRegistry, suggesterRegistry);
    }

    @Override
    public boolean canParse(Argument<TreeSet> argument, CollectionArgumentProfile collectionArgumentProfile) {
        if (!super.canParse(argument)) {
            return false;
        }

        Class<?> rawType = collectionArgumentProfile.getElementTypeToken().getRawType();

        boolean assignableFrom = Comparable.class.isAssignableFrom(rawType);

        if (!assignableFrom) {
            throw new IllegalArgumentException(rawType + " is not comparable!" + " (argument: " + argument + ")");
        }

        return assignableFrom;
    }

    @Override
    <E> Collector<E, ?, ? extends TreeSet<E>> getCollector(CollectionArgumentProfile collectionArgument, Invocation<SENDER> invocation) {
        return Collectors.toCollection(() -> new TreeSet<>());
    }

}

