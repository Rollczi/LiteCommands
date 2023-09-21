package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.unit.TestExecutor;
import dev.rollczi.litecommands.unit.TestSender;
import dev.rollczi.litecommands.unit.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MissingPermissionValidatorTest {

    final MissingPermissionValidator<TestSender> validator = new MissingPermissionValidator<>();

    @Test
    @DisplayName("should scan all permissions from root to executor and check if sender has them")
    void test() {
        Invocation<TestSender> invocation = TestUtil.invocation("test", "sub");

        CommandRoute<TestSender> test = assertSingle(CommandBuilder.<TestSender>create()
            .name("test")
            .applyMeta(meta -> meta.listEditor(Meta.PERMISSIONS)
                .add("permission.test")
                .apply()
            )
            .appendChild("sub", childContext -> childContext
                .applyMeta(meta -> meta.listEditor(Meta.PERMISSIONS).add("permission.sub").apply())
                .appendExecutor(parent -> new TestExecutor<>(parent).onMeta(meta -> meta.listEditor(Meta.PERMISSIONS).add("permission.sub.execute").apply()))));

        CommandRoute<TestSender> sub = assertPresent(test.getChild("sub"));
        CommandExecutor<TestSender> executor = sub.getExecutors().get(0);

        Flow result = validator.validate(invocation, executor);

        assertTrue(result.isTerminate());
        assertTrue(result.hasReason());

        MissingPermissions missingPermissions = assertInstanceOf(MissingPermissions.class, result.getReason());

        assertNotNull(missingPermissions);
        assertTrue(missingPermissions.isMissing());

        List<String> missing = missingPermissions.getPermissions();
        assertEquals(3, missing.size());

        assertTrue(missing.contains("permission.test"));
        assertTrue(missing.contains("permission.sub"));
        assertTrue(missing.contains("permission.sub.execute"));
    }

    private static <T> CommandRoute<T> assertSingle(CommandBuilder<T> context) {
        Collection<CommandRoute<T>> routeCollection = context.build(CommandRoute.createRoot());

        if (routeCollection.size() != 1) {
            throw new IllegalStateException("Expected single route, got " + routeCollection.size());
        }

        return routeCollection.iterator().next();
    }

    private static <T> T assertPresent(Optional<T> optional) {
        assertTrue(optional.isPresent());
        return optional.get();
    }

}