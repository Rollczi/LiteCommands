package dev.rollczi.litecommands.reflect.type;

import dev.rollczi.litecommands.reflect.IterableSuperClassResolver;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.RandomAccess;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import org.junit.jupiter.api.Test;

class IterableSuperClassResolverTest {

    @Test
    void test() {
        List<Class<?>> classes = new LinkedList<>();

        for (Class<?> clazz : new IterableSuperClassResolver(ArrayList.class)) {
            classes.add(clazz);
        }

        assertThat(classes)
            .containsExactly(
                ArrayList.class,
                AbstractList.class,
                AbstractCollection.class,
                Object.class,
                List.class,
                RandomAccess.class,
                Cloneable.class,
                Serializable.class,
                Collection.class,
                Iterable.class
            );
    }

}