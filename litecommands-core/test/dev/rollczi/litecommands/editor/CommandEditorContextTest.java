package dev.rollczi.litecommands.editor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CommandEditorContextTest {

    @Test
    void create() {
        CommandEditorContext<Object> context = CommandEditorContext.create();

        assertNotNull(context);
        assertInstanceOf(CommandEditorContextBase.class, context);
    }

    @Test
    void createRoot() {
        CommandEditorContext<Object> context = CommandEditorContext.createRoot();

        assertNotNull(context);
        assertInstanceOf(CommandEditorContextRootImpl.class, context);
    }

}