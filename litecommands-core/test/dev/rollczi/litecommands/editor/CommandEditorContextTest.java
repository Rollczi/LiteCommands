package dev.rollczi.litecommands.editor;

import dev.rollczi.litecommands.editor.CommandEditorContext;
import dev.rollczi.litecommands.editor.CommandEditorContextDummyPrefix;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandEditorContextTest {

    @Test
    void testSimpleRoute() {
        CommandEditorContext<?> context = CommandEditorContext.create()
            .routeName("test")
            .routeAliases(Arrays.asList("test1", "test2"));

        assertEquals("test", context.name());
        assertEquals(Arrays.asList("test1", "test2"), context.aliases());
    }

    @Test
    void testRouteWithChild() {
        CommandEditorContext<?> context = CommandEditorContext.create()
            .routeName("test child")
            .routeAliases(Arrays.asList("alias1 child", "alias2 c"));

        assertEquals("test", context.name());
        assertEquals(Arrays.asList("alias1", "alias2"), context.aliases());

        CommandEditorContextDummyPrefix<?> dummy = assertInstanceOf(CommandEditorContextDummyPrefix.class, context);

        assertEquals("child", dummy.parent.name());
        assertEquals(Arrays.asList("child", "c"), dummy.parent.aliases());
    }

    @Test
    void testNameAndAliasesMethods() {
        CommandEditorContext<Object> context = CommandEditorContext.create()
            .name("test")
            .aliases(Arrays.asList("test1", "test2"));

        assertEquals("test", context.name());
        assertEquals(Arrays.asList("test1", "test2"), context.aliases());
        assertEquals(Arrays.asList("test", "test1", "test2"), context.names());

        assertTrue(context.isNameOrAlias("test"));
        assertTrue(context.isNameOrAlias("test1"));
        assertTrue(context.isNameOrAlias("test2"));
    }

    @Test
    void testHasSimilarNamesMethod() {
        CommandEditorContext<Object> context = CommandEditorContext.create()
            .name("test")
            .aliases(Arrays.asList("test1", "test2"));

        CommandEditorContext<Object> other1 = CommandEditorContext.create()
            .name("test");

        assertTrue(context.hasSimilarNames(other1));

        CommandEditorContext<Object> other2 = CommandEditorContext.create()
            .name("other");

        assertFalse(context.hasSimilarNames(other2));
    }

    @Test
    void testEditApiMethodsWithRoute() {
        CommandEditorContext<?> context = CommandEditorContext.create()
            .routeName("test child")
            .routeAliases(Arrays.asList("alias1 child", "alias2 c"))
            .name("edited-test")
            .aliases(Arrays.asList("edited-alias1", "edited-alias2"));

        assertEquals("edited-test", context.name());
        assertEquals(Arrays.asList("edited-alias1", "edited-alias2"), context.aliases());

        CommandEditorContextDummyPrefix<?> dummy = assertInstanceOf(CommandEditorContextDummyPrefix.class, context);

        assertEquals("child", dummy.parent.name());
        assertEquals(Arrays.asList("child", "c"), dummy.parent.aliases());
    }

    @Test
    void testEditApiMethodsWithChild() {
        CommandEditorContext<?> context = CommandEditorContext.create()
            .routeName("test child")
            .routeAliases(Collections.singletonList("alias c"))
            .editChild("child", child -> child
                .name("edited-child")
                .aliases(Collections.singletonList("edited-c")));

        assertEquals("test", context.name());
        assertEquals(Collections.singletonList("alias"), context.aliases());

        CommandEditorContextDummyPrefix<?> dummy = assertInstanceOf(CommandEditorContextDummyPrefix.class, context);

        assertEquals("edited-child", dummy.parent.name());
        assertEquals(Collections.singletonList("edited-c"), dummy.parent.aliases());
    }

    @Test
    void testEnableAndDisableMethods() {
        CommandEditorContext<?> context = CommandEditorContext.create();
        assertTrue(context.isEnabled());

        context.disable();
        assertFalse(context.isEnabled());

        context.enable();
        assertTrue(context.isEnabled());
    }

    @Test
    void testEditChildMethod() {
        CommandEditorContext<Object> context = CommandEditorContext.create()
            .name("root")
            .appendChild(CommandEditorContext.create().name("first").aliases("sub-first"))
            .appendChild("second", child -> child.aliases("sub-second"));

        context.editChild("first", child -> {
            assertTrue(child.isNameOrAlias("sub-first"));

            return child;
        });

        context.editChild("second", child -> {
            assertTrue(child.isNameOrAlias("sub-second"));

            return child;
        });

        Collection<CommandEditorContext<Object>> children = context.children();
        assertEquals(2, children.size());

        assertThrows(IllegalArgumentException.class, () -> context.editChild("no-exists", child -> child));
    }

}