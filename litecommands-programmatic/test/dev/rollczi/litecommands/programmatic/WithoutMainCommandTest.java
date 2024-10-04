package dev.rollczi.litecommands.programmatic;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.unit.LiteCommandsTestFactory;
import dev.rollczi.litecommands.unit.TestPlatform;
import dev.rollczi.litecommands.unit.TestSender;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class WithoutMainCommandTest {

    private final Map<String, Boolean> userDatabase = new HashMap<>();

    TestPlatform platform = LiteCommandsTestFactory.startPlatform(builder -> builder.commands(
        new LiteCommand<TestSender>("user")
            .withoutExecutor()
            .subcommand("add", addCommand -> addCommand
                .argument(Argument.of("username", String.class))
                .executeReturn(context -> {
                    String username = context.argument("username", String.class);
                    userDatabase.put(username, true);
                    return "User " + username + " added successfully.";
                })
            )
            .subcommand("remove", removeCommand -> removeCommand
                .argument(Argument.of("username", String.class))
                .executeReturn(context -> {
                    String username = context.argument("username", String.class);
                    if (!userDatabase.containsKey(username)) {
                        return "User " + username + " not found.";
                    }

                    userDatabase.remove(username);
                    return "User " + username + " removed successfully.";
                })
            )
            .subcommand("status", statusCommand -> statusCommand
                .argument(Argument.of("username", String.class))
                .executeReturn(context -> {
                    String username = context.argument("username", String.class);
                    if (!userDatabase.containsKey(username)) {
                        return "User " + username + " does not exist.";
                    }

                    return "User " + username + " is active.";
                })
            )
        )
    );

    @Test
    public void testWithoutMainCommand() {
        platform.execute("user")
            .assertFailureInvalid(InvalidUsage.Cause.UNKNOWN_COMMAND);
    }

    @Test
    public void testAddUser() {
        platform.execute("user add john")
            .assertSuccess("User john added successfully.");
    }

    @Test
    public void testRemoveUser() {
        platform.execute("user add john").assertSuccess();
        platform.execute("user remove john")
            .assertSuccess("User john removed successfully.");
    }

    @Test
    public void testUserStatus() {
        platform.execute("user add alice").assertSuccess();
        platform.execute("user status alice")
            .assertSuccess("User alice is active.");
        platform.execute("user status bob")
            .assertSuccess("User bob does not exist.");
    }

}