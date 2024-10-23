package dev.rollczi.litecommands.annotations.cases;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.priority.Priority;
import dev.rollczi.litecommands.annotations.priority.PriorityValue;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.unit.TestSender;
import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DuplicatedSameTypeArgumentsTest extends LiteTestSpec {

    private final static Map<String, User> USERS = new HashMap<>();

    private static class User {
        private final String name;

        public User(String name) {
            this.name = name;
        }
    }

    @BeforeAll
    static void setup() {
        USERS.put("rollczi", new User("rollczi"));
        USERS.put("rollczi2", new User("rollczi2"));
    }

    static LiteTestConfig config = builder -> builder
        .argument(User.class, new UserArgument());

    private static class UserArgument extends ArgumentResolver<TestSender, User> {
        @Override
        protected ParseResult<User> parse(Invocation<TestSender> invocation, Argument<User> context, String argument) {
            if (!USERS.containsKey(argument)) {
                return ParseResult.failure("User not found");
            }

            return ParseResult.success(USERS.get(argument));
        }
    }

    @Command(name = "test")
    static class TestCommand {

        @Execute
        @Priority(PriorityValue.LOW)
        String execute(@Arg User user) {
            return user.name;
        }

        @Execute
        String execute(@Arg User user, @Arg User user2) {
            return user.name + " " + user2.name;
        }

    }

    @Test
    void testSuccess() {
        platform.execute("test rollczi")
            .assertSuccess("rollczi");

        platform.execute("test rollczi rollczi2")
            .assertSuccess("rollczi rollczi2");
    }

    @Test
    @DisplayName("Should ignore TOO_MANY_ARGUMENTS error and return a argument parse error")
    void testFailure() {
        platform.execute("test rollczi invalid")
            .assertFailure("User not found");
    }

}
