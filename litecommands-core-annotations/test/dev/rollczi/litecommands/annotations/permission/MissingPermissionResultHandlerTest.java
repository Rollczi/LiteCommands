package dev.rollczi.litecommands.annotations.permission;

import dev.rollczi.litecommands.annotations.LiteConfig;
import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.Command;
import dev.rollczi.litecommands.command.executor.Execute;
import dev.rollczi.litecommands.permission.Permission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MissingPermissionResultHandlerTest extends LiteTestSpec {

    static AtomicBoolean missingPermissionHandler = new AtomicBoolean(false);
    static AtomicBoolean throwHandled = new AtomicBoolean(false);
    static AtomicBoolean invalidHandler = new AtomicBoolean(false);

    static LiteConfig config = builder -> builder
        .missingPermission((invocation, missingPermissions, chain) -> {
            if (missingPermissions.getPermissions().contains("test.permission.throw")) {
                throw new RuntimeException("Missing permission");
            }

            missingPermissionHandler.set(true);
        })
        .invalidUsage((invocation, command, chain) -> invalidHandler.set(true))
            .exception(RuntimeException.class, (invocation, exception) -> throwHandled.set(true));

    @Command(name = "test sub")
    @Permission("test.permission")
    static class MultilevelCommandTest {

        @Execute
        void execute() {}

        @Execute(name = "throwException")
        @Permission("test.permission.throw")
        void throwException() {}

    }

    @Command(name = "single")
    @Permission("test.permission")
    static class TestCommand {

        @Execute
        void execute(@Arg int x) {}

        @Execute(name = "test")
        void executeTest(@Arg int y) {}

    }

    @BeforeEach
    void setUp() {
        missingPermissionHandler.set(false);
        throwHandled.set(false);
        invalidHandler.set(false);
    }

    @Test
    void test() {
        platform.execute("test sub")
            .assertMissingPermission("test.permission");

        assertTrue(missingPermissionHandler.get());
    }

    @Test
    void testThrow() {
        platform.execute("test sub throwException")
            .assertThrows(RuntimeException.class);

        assertTrue(throwHandled.get());
    }

    @Test
    void testInvalidHandler() {
        platform.execute("test")
            .assertMissingPermission("test.permission");

        assertFalse(invalidHandler.get());
    }

    @Test
    void testSingle() {
        platform.execute("single")
            .assertMissingPermission("test.permission");

        assertTrue(missingPermissionHandler.get());
    }

}
