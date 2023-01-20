package dev.rollczi.litecommands.modern.command.editor;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class CommandEditorContextTest {

    @Test
    void testSimpleRoute() {
        CommandEditorContext context = CommandEditorContext.empty()
            .routeName("test")
            .routeAliases(Arrays.asList("test1", "test2"));

        assertEquals("test", context.name());
        assertEquals(Arrays.asList("test1", "test2"), context.aliases());
    }

    @Test
    void testRouteWithChild() {
        CommandEditorContext context = CommandEditorContext.empty()
            .routeName("test child")
            .routeAliases(Arrays.asList("alias1 child", "alias2 c"));

        assertEquals("test", context.name());
        assertEquals(Arrays.asList("alias1", "alias2"), context.aliases());

        CommandEditorContextDummy dummy = assertInstanceOf(CommandEditorContextDummy.class, context);

        assertEquals("child", dummy.parent.name());
        assertEquals(Arrays.asList("child", "c"), dummy.parent.aliases());
    }

    @Test
    void testEditApiMethods() {
        CommandEditorContext context = CommandEditorContext.empty()
            .name("test")
            .aliases(Arrays.asList("test1", "test2"));

        assertEquals("test", context.name());
        assertEquals(Arrays.asList("test1", "test2"), context.aliases());
    }

    @Test
    void testEditApiMethodsWithRoute() {
        CommandEditorContext context = CommandEditorContext.empty()
            .routeName("test child")
            .routeAliases(Arrays.asList("alias1 child", "alias2 c"))
            .name("edited-test")
            .aliases(Arrays.asList("edited-alias1", "edited-alias2"));

        assertEquals("edited-test", context.name());
        assertEquals(Arrays.asList("edited-alias1", "edited-alias2"), context.aliases());

        CommandEditorContextDummy dummy = assertInstanceOf(CommandEditorContextDummy.class, context);

        assertEquals("child", dummy.parent.name());
        assertEquals(Arrays.asList("child", "c"), dummy.parent.aliases());
    }

    @Test
    void testEditApiMethodsWithChild() {
        CommandEditorContext context = CommandEditorContext.empty()
            .routeName("test child")
            .routeAliases(Collections.singletonList("alias c"))
            .editChild("child", child -> child
                .name("edited-child")
                .aliases(Collections.singletonList("edited-c")));

        assertEquals("test", context.name());
        assertEquals(Collections.singletonList("alias"), context.aliases());

        CommandEditorContextDummy dummy = assertInstanceOf(CommandEditorContextDummy.class, context);

        assertEquals("edited-child", dummy.parent.name());
        assertEquals(Collections.singletonList("edited-c"), dummy.parent.aliases());
    }

}