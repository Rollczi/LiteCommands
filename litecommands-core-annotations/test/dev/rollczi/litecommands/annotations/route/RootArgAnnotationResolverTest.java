package dev.rollczi.litecommands.annotations.route;

import dev.rollczi.litecommands.annotations.command.RootCommand;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RootArgAnnotationResolverTest {

    RootCommand.AnnotationResolver<?> resolver = new RootCommand.AnnotationResolver<>();

    @RootCommand
    static class Command {}

    @Test
    void test() {
        Command command = new Command();
        RootCommand rootCommand = Command.class.getAnnotation(RootCommand.class);

        CommandBuilder<?> context = resolver.resolve(command, rootCommand, CommandBuilder.create());

        assertEquals("", context.name());
    }

}