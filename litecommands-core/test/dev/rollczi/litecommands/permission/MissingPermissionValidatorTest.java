package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.command.CommandExecutor;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.editor.CommandEditorContext;
import dev.rollczi.litecommands.editor.CommandEditorExecutorBuilder;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.unit.TestExecutor;
import dev.rollczi.litecommands.unit.TestSender;
import dev.rollczi.litecommands.unit.TestUtil;
import dev.rollczi.litecommands.validator.ValidatorResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MissingPermissionValidatorTest {

    MissingPermissionValidator<TestSender> validator = new MissingPermissionValidator<>();

    @Test
    @DisplayName("should scan all permissions from root to executor and check if sender has them")
    void test() {
        Invocation<TestSender> invocation = TestUtil.invocation("test", "sub");

        CommandRoute<TestSender> test = assertSingle(CommandEditorContext.<TestSender>create()
            .name("test")
            .applyMeta(meta -> meta.listEditor(CommandMeta.PERMISSIONS)
                .add("permission.test")
                .apply()
            )
            .appendChild("sub", childContext -> {
                CommandEditorExecutorBuilder<TestSender> builder = new CommandEditorExecutorBuilder<TestSender>(new TestExecutor<>())
                    .applyMeta(meta -> meta.listEditor(CommandMeta.PERMISSIONS).add("permission.sub.execute").apply())
                    .applyMeta(meta -> meta.listEditor(CommandMeta.PERMISSIONS_EXCLUDED).add("permission.sub.toexclude").apply());

                return childContext
                    .applyMeta(meta -> meta.put(CommandMeta.PERMISSIONS, Arrays.asList("permission.sub", "permission.sub.toexclude")))
                    .appendExecutor(builder);
            }));

        CommandRoute<TestSender> sub = assertPresent(test.getChildren("sub"));
        CommandExecutor<TestSender> executor = sub.getExecutors().get(0);

        ValidatorResult result = validator.validate(invocation, sub, executor);

        assertTrue(result.isInvalid());
        assertTrue(result.hasInvalidResult());

        MissingPermissions missingPermissions = assertInstanceOf(MissingPermissions.class, result.getInvalidResult());
        assertTrue(missingPermissions.isMissing());

        List<String> missing = missingPermissions.getPermissions();
        assertEquals(3, missing.size());

        assertTrue(missing.contains("permission.test"));
        assertTrue(missing.contains("permission.sub"));
        assertTrue(missing.contains("permission.sub.execute"));
    }

    private static <T> CommandRoute<T> assertSingle(CommandEditorContext<T> context) {
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