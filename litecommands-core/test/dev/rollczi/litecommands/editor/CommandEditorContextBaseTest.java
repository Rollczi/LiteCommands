package dev.rollczi.litecommands.editor;

import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unchecked rawtypes OptionalUsedAsFieldOrParameterType")
class CommandEditorContextBaseTest {

    @Test
    @DisplayName("Should set name and aliases")
    void testNameAndAliasesMethods() {
        CommandEditorContext<Object> context = CommandEditorContext.create()
            .name("name")
            .aliases(Arrays.asList("alias", "other-alias"));

        assertEquals("name", context.name());
        assertEquals(Arrays.asList("alias", "other-alias"), context.aliases());
        assertEquals(Arrays.asList("name", "alias", "other-alias"), context.names());
    }

    @Test
    @DisplayName("Should return true when is name or alias")
    void testIsNameOrAlias() {
        CommandEditorContext<Object> context = CommandEditorContext.create()
            .name("name")
            .aliases(Arrays.asList("alias", "other-alias"));

        assertTrue(context.isNameOrAlias("name"));
        assertTrue(context.isNameOrAlias("alias"));
        assertTrue(context.isNameOrAlias("other-alias"));
    }

    @Test
    @DisplayName("Should return false when is not name or alias")
    void testIsNotNameOrAlias() {
        CommandEditorContext<Object> context = CommandEditorContext.create()
            .name("name")
            .aliases(Arrays.asList("alias", "other-alias"));

        assertFalse(context.isNameOrAlias("not-name"));
    }

    @Test
    @DisplayName("Should return true when has similar names")
    void testHasSimilarNames() {
        CommandEditorContext<Object> context = CommandEditorContext.create()
            .name("name")
            .aliases(Arrays.asList("alias", "other-alias"));

        CommandEditorContext<Object> other = CommandEditorContext.create()
            .name("name");

        assertTrue(context.hasSimilarNames(other));
    }

    @Test
    @DisplayName("Should return false when has not similar names")
    void testHasNotSimilarNames() {
        CommandEditorContext<Object> context = CommandEditorContext.create()
            .name("name")
            .aliases(Arrays.asList("alias", "other-alias"));

        CommandEditorContext<Object> other = CommandEditorContext.create()
            .name("other");

        assertFalse(context.hasSimilarNames(other));
    }

    @Test
    @DisplayName("Should return child by name and aliases")
    void testPutAndGetChild() {
        CommandEditorContextBase context = new CommandEditorContextBase(){};
        CommandEditorContext expectedChild = new CommandEditorContextBase(){}
            .name("test")
            .aliases("test-alias");
        context.appendChild(expectedChild);

        assertOptionalEquals(expectedChild, context.getChild("test"));
        assertOptionalEquals(expectedChild, context.getChild("test-alias"));
    }

    @Test
    @DisplayName("Should return Optional.empty() when child not exists")
    void testGetChildWhenNotExists() {
        CommandEditorContextBase context = new CommandEditorContextBase(){};

        Optional<CommandEditorContext> child = context.getChild("test");

        assertFalse(child.isPresent());
    }

    @Test
    @DisplayName("Should append child and set name and aliases")
    void testAppendChild() {
        CommandEditorContext<Object> context = CommandEditorContext.create()
            .name("root")
            .appendChild(CommandEditorContext.create().name("first").aliases("sub-first"))
            .appendChild("second", child -> child.aliases("sub-second"));

        CommandEditorContext<?> first = assertOptionalPresent(context.getChild("first"));
        assertTrue(first.isNameOrAlias("sub-first"));

        CommandEditorContext<?> second = assertOptionalPresent(context.getChild("second"));
        assertTrue(second.isNameOrAlias("sub-second"));
    }

    @Test
    @DisplayName("Should merge two children with same name")
    void testMergeChildren() {
        CommandEditorContextBase context = new CommandEditorContextBase(){};
        CommandEditorContext expectedChild = new CommandEditorContextBase(){}
            .name("test")
            .aliases("test-alias");
        context.appendChild(expectedChild);

        CommandEditorContext newChild = new CommandEditorContextBase(){}
            .name("test")
            .aliases("test-other");
        context.appendChild(newChild);

        assertOptionalEquals(expectedChild, context.getChild("test"));
        assertOptionalEquals(expectedChild, context.getChild("test-alias"));
        assertOptionalEquals(expectedChild, context.getChild("test-other"));
    }



    @Test
    @DisplayName("Should set one-level route name and aliases")
    void testRouteNameAndRouteAliasesMethods() {
        CommandEditorContext context = new CommandEditorContextBase(){}
            .routeName("test")
            .routeAliases(Arrays.asList("test1", "test2"));

        assertEquals("test", context.name());
        assertEquals(Arrays.asList("test1", "test2"), context.aliases());
    }

    @Test
    @DisplayName("Should set two-level route name and aliases")
    void testRouteNameAndRouteAliasesMethods2() {
        CommandEditorContext context = new CommandEditorContextBase(){}
            .routeName("test child")
            .routeAliases(Arrays.asList("alias1 child", "alias2 c"));

        assertEquals("test", context.name());
        assertEquals(Arrays.asList("alias1", "alias2"), context.aliases());

        CommandEditorContextDummyPrefix dummy = assertInstanceOf(CommandEditorContextDummyPrefix.class, context);

        assertEquals("child", dummy.parent.name());
        assertEquals(Arrays.asList("child", "c"), dummy.parent.aliases());
    }

    @Test
    @DisplayName("Should set three-level route name and aliases")
    void testRouteNameAndRouteAliasesMethods3() {
        CommandEditorContext context = new CommandEditorContextBase(){}
            .routeName("test child grandchild")
            .routeAliases(Arrays.asList("alias child grandchild-alias", "other-alias c gc"));

        assertEquals("test", context.name());
        assertEquals(Arrays.asList("alias", "other-alias"), context.aliases());

        CommandEditorContextDummyPrefix dummy = assertInstanceOf(CommandEditorContextDummyPrefix.class, context);

        assertEquals("child", dummy.parent.name());
        assertEquals(Arrays.asList("child", "c"), dummy.parent.aliases());

        CommandEditorContextDummyPrefix dummy2 = assertInstanceOf(CommandEditorContextDummyPrefix.class, dummy.parent);

        assertEquals("grandchild", dummy2.parent.name());
        assertEquals(Arrays.asList("grandchild-alias", "gc"), dummy2.parent.aliases());
    }

    @Test
    @DisplayName("Should rename head name and aliases for tow-level context")
    void testEditRouteNameAndRouteAliasesMethods() {
        CommandEditorContext<?> context = new CommandEditorContextBase(){}
            .routeName("name child")
            .routeAliases(Arrays.asList("alias child", "other-alias c"))
            .name("edited-name")
            .aliases(Arrays.asList("edited-alias", "edited-other-alias"));

        assertEquals("edited-name", context.name());
        assertEquals(Arrays.asList("edited-alias", "edited-other-alias"), context.aliases());

        CommandEditorContextDummyPrefix<?> dummy = assertInstanceOf(CommandEditorContextDummyPrefix.class, context);

        assertEquals("child", dummy.parent.name());
        assertEquals(Arrays.asList("child", "c"), dummy.parent.aliases());
    }

    @Test
    @DisplayName("Should edit child name and aliases on one-level context")
    void testEditChildByEditApiMethods() {
        CommandEditorContext<?> context = new CommandEditorContextBase<TestSender>(){}
            .name("name")
            .aliases(Collections.singletonList("alias"))
            .appendChild("child", child -> child)
            .editChild("child", child -> child
                .name("edited-child")
                .aliases(Collections.singletonList("edited-c"))
            );

        assertEquals("name", context.name());
        assertEquals(Collections.singletonList("alias"), context.aliases());

        CommandEditorContext<?> child = assertOptionalPresent(context.getChild("child"));

        assertEquals("edited-child", child.name());
        assertEquals(Collections.singletonList("edited-c"), child.aliases());
    }

    @Test
    @DisplayName("Should edit child name and aliases on two-level context")
    void testEditChildByEditApiMethods2() {
        CommandEditorContext<?> context = new CommandEditorContextBase<TestSender>(){}
            .routeName("name child")
            .routeAliases(Collections.singletonList("alias c"))
            .editChild("child", child -> child
                .name("edited-child")
                .aliases(Collections.singletonList("edited-c"))
            );

        assertEquals("name", context.name());
        assertEquals(Collections.singletonList("alias"), context.aliases());

        CommandEditorContextDummyPrefix<?> dummy = assertInstanceOf(CommandEditorContextDummyPrefix.class, context);

        assertEquals("edited-child", dummy.parent.name());
        assertEquals(Collections.singletonList("edited-c"), dummy.parent.aliases());
    }

    @Test
    @DisplayName("Should throw exception when edited child not exists")
    void testEditChildByEditApiMethodsWhenChildNotExists() {
        CommandEditorContext<?> context = new CommandEditorContextBase<TestSender>(){};

        assertThrows(IllegalArgumentException.class, () -> context.editChild("child", child -> child));
    }

    @Test
    @DisplayName("Should enable and disable command")
    void testEnableAndDisableCommand() {
        CommandEditorContext<?> context = new CommandEditorContextBase<TestSender>(){};
        assertTrue(context.isEnabled());

        context.disable();
        assertFalse(context.isEnabled());

        context.enable();
        assertTrue(context.isEnabled());
    }

    private void assertOptionalEquals(CommandEditorContext expected, Optional<CommandEditorContext> optional) {
        assertTrue(optional.isPresent());
        assertEquals(expected, optional.get());
    }

    private <T> T assertOptionalPresent(Optional<T> optional) {
        assertTrue(optional.isPresent());
        return optional.get();
    }

}