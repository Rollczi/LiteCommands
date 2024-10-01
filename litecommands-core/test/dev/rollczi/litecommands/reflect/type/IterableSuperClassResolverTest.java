package dev.rollczi.litecommands.reflect.type;

import dev.rollczi.litecommands.argument.profile.ProfiledMultipleArgumentResolver;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolverBase;
import dev.rollczi.litecommands.argument.resolver.MultipleArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.collector.AbstractCollectorArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.collector.StackArgumentResolver;
import dev.rollczi.litecommands.argument.suggester.Suggester;
import dev.rollczi.litecommands.range.Rangeable;
import dev.rollczi.litecommands.reflect.IterableSuperClassResolver;
import java.util.LinkedList;
import java.util.List;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import org.junit.jupiter.api.Test;

class IterableSuperClassResolverTest {

    @Test
    void test() {
        List<Class<?>> classes = new LinkedList<>();

        for (Class<?> clazz : new IterableSuperClassResolver(StackArgumentResolver.class)) {
            classes.add(clazz);
        }

        assertThat(classes)
            .containsExactlyInAnyOrder(
                StackArgumentResolver.class,
                AbstractCollectorArgumentResolver.class,
                ProfiledMultipleArgumentResolver.class,
                MultipleArgumentResolver.class,
                ArgumentResolverBase.class,
                Parser.class,
                Suggester.class,
                Rangeable.class
            );
    }

}