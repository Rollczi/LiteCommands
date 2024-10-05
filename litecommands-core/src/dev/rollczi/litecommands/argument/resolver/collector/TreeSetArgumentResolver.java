
package dev.rollczi.litecommands.argument.resolver.collector;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import java.util.TreeSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class TreeSetArgumentResolver<SENDER> extends AbstractCollectorArgumentResolver<SENDER, TreeSet> {

    public TreeSetArgumentResolver(ParserRegistry<SENDER> parserRegistry, SuggesterRegistry<SENDER> suggesterRegistry) {
        super(parserRegistry, suggesterRegistry);
    }

    @Override
    public boolean canParse(Argument<TreeSet> argument, VarargsProfile varargsProfile) {
        if (!super.canParse(argument)) {
            return false;
        }

        TypeToken<?> elementType = varargsProfile.getElementType();

        if (elementType.isInstanceOf(Comparable.class)) {
            return true;
        }

        throw new IllegalArgumentException(elementType.getRawType().getSimpleName() + " is not comparable!" + " (argument: " + argument + ")");
    }

    @Override
    <E> Collector<E, ?, ? extends TreeSet<E>> getCollector(VarargsProfile varargsProfile, Invocation<SENDER> invocation) {
        return Collectors.toCollection(() -> new TreeSet<>());
    }

}

