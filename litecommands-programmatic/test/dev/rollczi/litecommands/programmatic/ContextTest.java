package dev.rollczi.litecommands.programmatic;

import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.unit.LiteCommandsTestFactory;
import dev.rollczi.litecommands.unit.TestPlatform;
import dev.rollczi.litecommands.unit.TestPlatformSender;
import dev.rollczi.litecommands.unit.TestSender;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ContextTest {

    static class User {
        private final String name;

        public User(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    static class UserContextual<S> implements ContextProvider<S, User> {
        @Override
        public ContextResult<User> provide(Invocation<S> invocation) {
            String name = invocation.platformSender().getName();

            if (!name.startsWith("user:")) {
                return ContextResult.error("Invalid user name");
            }

            return ContextResult.ok(() -> new User(name));
        }
    }

    TestPlatform testPlatform = LiteCommandsTestFactory.startPlatform(builder -> builder
        .resultUnexpected((invocation, result, chain) -> {})
        .context(User.class, new UserContextual<>())
        .commands(
            new LiteCommand<TestSender>("test")
                .context("user", User.class)
                .executeReturn(context -> context.context("user", User.class))
        ));

    @Test
    @DisplayName("Should execute command with context")
    public void testSuccess() {
        User user = testPlatform.execute(TestPlatformSender.named("user:Rollczi"),"test")
            .assertSuccessAs(User.class);

        assertThat(user.getName()).isEqualTo("user:Rollczi");
    }

    @Test
    @DisplayName("Should fail to execute command with context")
    public void testFail() {
        testPlatform.execute(TestPlatformSender.named("Rollczi"),"test")
            .assertFailure("Invalid user name");
    }

}
