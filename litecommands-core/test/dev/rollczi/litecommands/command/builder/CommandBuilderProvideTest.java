package dev.rollczi.litecommands.command.builder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CommandBuilderProvideTest {

    @Test
    void create() {
        CommandBuilder<Object> context = CommandBuilder.create();

        assertNotNull(context);
        assertInstanceOf(CommandBuilderBase.class, context);
    }

    @Test
    void createRoot() {
        CommandBuilder<Object> context = CommandBuilder.createRoot();

        assertNotNull(context);
        assertInstanceOf(CommandBuilderRootImpl.class, context);
    }

}