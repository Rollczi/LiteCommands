package dev.rollczi.litecommands.argument.resolver.collector;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class ArrayArgumentResolver<SENDER> extends AbstractCollectorArgumentResolver<SENDER, Object> {

    public ArrayArgumentResolver(ParserRegistry<SENDER> parserRegistry, SuggesterRegistry<SENDER> suggesterRegistry) {
        super(parserRegistry, suggesterRegistry);
    }

    @Override
    <E> Collector<E, ?, Object> getCollector(CollectionArgumentProfile collectionArgument, Invocation<SENDER> invocation) {
        return new ArrayCollector<>(getElementType(collectionArgument));
    }

    @Override
    public boolean canParse(Argument<Object> argument, CollectionArgumentProfile collectionArgument) {
        return argument.getType().isArray();
    }

    private static class ArrayCollector<E> implements Collector<E, ArrayList<E>, Object> {

        private final TypeToken<E> componentType;

        private ArrayCollector(TypeToken<E> componentType) {
            this.componentType = componentType;
        }

        @Override
        public Supplier<ArrayList<E>> supplier() {
            return () -> new ArrayList<>();
        }

        @Override
        public BiConsumer<ArrayList<E>, E> accumulator() {
            return (objects, o) -> objects.add(o);
        }

        @Override
        public BinaryOperator<ArrayList<E>> combiner() {
            return (objects, objects2) -> {
                objects.addAll(objects2);
                return objects;
            };
        }

        @Override
        public Function<ArrayList<E>, Object> finisher() {
            return objects -> {
                Object array = Array.newInstance(componentType.getRawType(), objects.size());
                for (int i = 0; i < objects.size(); i++) {
                    Array.set(array, i, objects.get(i));
                }
                return array;
            };
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.emptySet();
        }

    }

}
