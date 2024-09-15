package dev.rollczi.litecommands.annotations.route;

import dev.rollczi.litecommands.annotations.command.RootCommand;
import dev.rollczi.litecommands.annotations.command.RootCommandAnnotationProcessor;
import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("unchecked rawtypes")
class RootArgAnnotationResolverTest {

    final RootCommandAnnotationProcessor<?> resolver = new RootCommandAnnotationProcessor<>();

    @RootCommand
    static class Command {}

    @Test
    void test() {
        Command command = new Command();
        RootCommand rootCommand = Command.class.getAnnotation(RootCommand.class);

        AnnotationInvoker invoker = resolver.process(new AnnotationInvoker() {

            CommandBuilder builder = CommandBuilder.create();

            @Override
            public CommandBuilder getResult() {
                return builder;
            }

            @Override
            public AnnotationInvoker onClass(Class annotationType, AnnotationProcessor.ClassListener listener) {
                builder = listener.call(Command.class, rootCommand, builder);
                return this;
            }
        });

        assertEquals("", invoker.getResult().name());
    }

}