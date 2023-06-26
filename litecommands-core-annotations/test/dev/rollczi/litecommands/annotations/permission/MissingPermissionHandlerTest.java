package dev.rollczi.litecommands.annotations.permission;

import dev.rollczi.litecommands.annotations.LiteConfig;
import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MissingPermissionHandlerTest extends LiteTestSpec {

    static AtomicBoolean called = new AtomicBoolean(false);
    static AtomicBoolean throwHandled = new AtomicBoolean(false);

    static LiteConfig config = builder -> builder
            .missingPermission((invocation, missingPermissions) -> {
                if (missingPermissions.getPermissions().contains("test.permission.throw")) {
                    throw new RuntimeException("Missing permission");
                }

                called.set(true);
            })
            .exception(RuntimeException.class, (invocation, exception) -> throwHandled.set(true));

    @Command(name = "test")
    @Permission("test.permission")
    static class CommandTest {

        @Execute
        void execute() {}

        @Execute(name = "throwException")
        @Permission("test.permission.throw")
        void throwException() {}

    }

    @Test
    void test() {
        platform.execute("test")
            .assertMissingPermission("test.permission");

        assertTrue(called.get());
    }

    @Test
    void testThrow() {
        platform.execute("test throwException")
            .assertThrows(RuntimeException.class);

        assertTrue(throwHandled.get());
    }

}
