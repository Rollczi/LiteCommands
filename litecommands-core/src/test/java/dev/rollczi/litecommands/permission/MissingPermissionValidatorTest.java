package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.command.CommandExecutor;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.editor.CommandEditorContext;
import dev.rollczi.litecommands.editor.CommandEditorExecutorBuilder;
import dev.rollczi.litecommands.test.TestExecutor;
import dev.rollczi.litecommands.test.FakeSender;
import dev.rollczi.litecommands.test.TestUtil;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.test.AssertCommand;
import dev.rollczi.litecommands.validator.CommandValidatorResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static dev.rollczi.litecommands.test.Assert.assertPresent;
import static org.junit.jupiter.api.Assertions.*;

class MissingPermissionValidatorTest {

    MissingPermissionValidator<FakeSender> validator = new MissingPermissionValidator<>();

    @Test
    @DisplayName("should scan all permissions from root to executor and check if sender has them")
    void test() {
        Invocation<FakeSender> invocation = TestUtil.invocation("test", "sub");

        CommandRoute<FakeSender> test = AssertCommand.assertSingle(CommandEditorContext.<FakeSender>create()
            .name("test")
            .applyMeta(meta -> meta.appendToList(CommandMeta.PERMISSIONS, "permission.test"))
            .appendChild("sub", childContext -> {
                CommandEditorExecutorBuilder<FakeSender> builder = new CommandEditorExecutorBuilder<FakeSender>(new TestExecutor<>())
                    .applyMeta(meta -> meta.appendToList(CommandMeta.PERMISSIONS, "permission.sub.execute"))
                    .applyMeta(meta -> meta.appendToList(CommandMeta.PERMISSIONS_EXCLUDED, "permission.sub.toexclude"));

                return childContext
                    .applyMeta(meta -> meta.put(CommandMeta.PERMISSIONS, Arrays.asList("permission.sub", "permission.sub.toexclude")))
                    .appendExecutor(builder);
            }));

        CommandRoute<FakeSender> sub = assertPresent(test.getChildren("sub"));
        CommandExecutor<FakeSender> executor = sub.getExecutors().get(0);

        CommandValidatorResult result = validator.validate(invocation, sub, executor);

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

}