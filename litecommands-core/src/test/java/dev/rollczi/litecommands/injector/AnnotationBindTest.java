package dev.rollczi.litecommands.injector;

import dev.rollczi.litecommands.TestFactory;
import dev.rollczi.litecommands.TestPlatform;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.section.Section;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

class AnnotationBindTest {

    @Retention(RetentionPolicy.RUNTIME)
    @interface Annotation {
        String value();
    }

    @Section(route = "test")
    static class Command {
        @Execute
        String execute(@Annotation("custom") String test) {
            return test;
        }
    }

    TestPlatform platform = TestFactory.create(builder -> builder
            .command(Command.class)
            .annotatedBind(String.class, Annotation.class, (parameter, annotation) -> annotation.value())
    );

    @Test
    void test() {
        platform.execute("test").assertResult("custom");
    }

}
