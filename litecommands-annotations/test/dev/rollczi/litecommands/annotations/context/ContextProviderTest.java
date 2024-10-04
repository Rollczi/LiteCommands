package dev.rollczi.litecommands.annotations.context;

import dev.rollczi.litecommands.unit.TestPlatformSender;
import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.test.LiteTestGuild;
import dev.rollczi.litecommands.annotations.test.LiteTestUser;
import dev.rollczi.litecommands.context.ContextChainAccessor;
import dev.rollczi.litecommands.context.ContextChainedProvider;
import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ContextProviderTest extends LiteTestSpec {

    private static final String USER_IS_NOT_IN_A_GUILD = "User is not in a guild";

    static LiteTestConfig config = builder -> builder.advanced()
        .context(LiteTestUser.class, new UserContextual<>())
        .context(LiteTestGuild.class, new GuildContextual<>());

    static Map<String, LiteTestUser> USERS = new ConcurrentHashMap<>();

    static class UserContextual<S> implements ContextProvider<S, LiteTestUser> {

        @Override
        public ContextResult<LiteTestUser> provide(Invocation<S> invocation) {
            LiteTestUser LiteTestUser = USERS.computeIfAbsent(invocation.platformSender().getName(), name -> new LiteTestUser(name));
            return ContextResult.ok(() -> LiteTestUser);
        }

    }

    static class GuildContextual<S> implements ContextChainedProvider<S, LiteTestGuild> {

        @Override
        public ContextResult<LiteTestGuild> provide(Invocation<S> invocation, ContextChainAccessor<S> accessor) {
            return accessor.provideContext(LiteTestUser.class, invocation)
                .flatMap(user -> user.getGuild() == null
                    ? ContextResult.error(USER_IS_NOT_IN_A_GUILD)
                    : ContextResult.ok(() -> user.getGuild())
                );
        }

    }

    @Command(name = "command")
    static class TestCommand {

        @Execute(name = "user")
        String execute(@Context LiteTestUser user) {
            return user.getName();
        }

        @Execute(name = "guild get")
        String execute(@Context LiteTestGuild guild) {
            return guild.getName();
        }

        @Execute(name = "guild set")
        String execute(@Context LiteTestUser user, @Arg String name) {
            user.setGuild(name);
            return "Guild set: " + name;
        }

        @Execute(name = "guild clear")
        String executeClear(@Sender LiteTestUser user) {
            user.setGuild(null);
            return "Guild cleared :" + user.getName();
        }
    }

    @Test
    @DisplayName("Should provide user context")
    void testUserContext() {
        platform.execute(TestPlatformSender.named("Rollczi"),"command user")
            .assertSuccess("Rollczi");
    }

    @Test
    @DisplayName("Should provide guild context")
    void testGuildContext() {
        platform.execute("command guild get")
            .assertFailure(USER_IS_NOT_IN_A_GUILD);

        platform.execute("command guild set test")
            .assertSuccess("Guild set: test");

        platform.execute("command guild get")
            .assertSuccess("test");

        platform.execute(TestPlatformSender.named("Matis"), "command guild clear")
            .assertSuccess("Guild cleared :Matis");
    }

}
