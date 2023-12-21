package dev.rollczi.litecommands.argument.resolver.collector;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class ArrayArgumentResolver<SENDER> extends AbstractCollectorArgumentResolver<SENDER, Object, Object> {

    public ArrayArgumentResolver(ParserRegistry<SENDER> parserRegistry, SuggesterRegistry<SENDER> suggesterRegistry) {
        super(parserRegistry, suggesterRegistry);
    }

    @Override
    Collector<Object, ?, Object> getCollector(CollectorArgument<Object> collectorArgument, Invocation<SENDER> invocation) {
        return new ArrayCollector(getElementType(collectorArgument, invocation));
    }

    @Override
    protected Class<Object> getElementType(CollectorArgument<Object> context, Invocation<SENDER> invocation) {
        Class<Object> arrayType = context.getWrapperFormat().getParsedType();

        if (!arrayType.isArray()) {
            throw new IllegalArgumentException("ArrayArgumentResolver can only parse arrays");
        }

        Class<Object> componentType = (Class<Object>) arrayType.getComponentType();

        if (componentType == null) {
            throw new IllegalArgumentException("ArrayArgumentResolver cannot parse array of null");
        }

        return componentType;
    }

    @Override
    public boolean canParse(Invocation<SENDER> invocation, Argument<Object> argument, Class<RawInput> inputType) {
        return argument.getWrapperFormat().getParsedType().isArray();
    }

    private static class ArrayCollector implements Collector<Object, ArrayList<Object>, Object> {

        private final Class<Object> componentType;

        private ArrayCollector(Class<Object> componentType) {
            this.componentType = componentType;
        }

        @Override
        public Supplier<ArrayList<Object>> supplier() {
            return () -> new ArrayList<>();
        }

        @Override
        public BiConsumer<ArrayList<Object>, Object> accumulator() {
            return (objects, o) -> objects.add(o);
        }

        @Override
        public BinaryOperator<ArrayList<Object>> combiner() {
            return (objects, objects2) -> {
                objects.addAll(objects2);
                return objects;
            };
        }

        @Override
        public Function<ArrayList<Object>, Object> finisher() {
            return objects -> {
                Object array = Array.newInstance(componentType, objects.size());
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
