package dev.rollczi.litecommands.modern.permission;

import dev.rollczi.litecommands.modern.command.CommandExecutor;
import dev.rollczi.litecommands.modern.command.CommandRoute;
import dev.rollczi.litecommands.modern.editor.CommandEditorContext;
import dev.rollczi.litecommands.modern.editor.CommandEditorExecutorBuilder;
import dev.rollczi.litecommands.modern.env.FakeExecutor;
import dev.rollczi.litecommands.modern.env.FakeSender;
import dev.rollczi.litecommands.modern.env.TestUtil;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.meta.CommandMeta;
import dev.rollczi.litecommands.modern.env.AssertCommand;
import dev.rollczi.litecommands.modern.validator.CommandValidatorResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static dev.rollczi.litecommands.modern.env.Assert.assertPresent;
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
                CommandEditorExecutorBuilder<FakeSender> builder = new CommandEditorExecutorBuilder<>(new FakeExecutor<>());

                builder.getMeta()
                    .appendToList(CommandMeta.PERMISSIONS, "permission.sub.execute")
                    .appendToList(CommandMeta.PERMISSIONS_EXCLUDED, "permission.sub.toexclude");

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