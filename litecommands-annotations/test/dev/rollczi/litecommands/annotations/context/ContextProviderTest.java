package dev.rollczi.litecommands.annotations.context;

import dev.rollczi.litecommands.annotations.LiteConfig;
import dev.rollczi.litecommands.annotations.LiteTestSpec;
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

    static LiteConfig config = builder -> builder
        .context(LiteTestUser.class, new UserContextual<>())
        .context(LiteTestGuild.class, new GuildContextual<>());

    static Map<String, LiteTestUser> USERS = new ConcurrentHashMap<>();

    static class UserContextual<S> implements ContextProvider<S, LiteTestUser> {

        @Override
        public ContextResult<LiteTestUser> provide(Invocation<S> invocation) {
            LiteTestUser LiteTestUser = USERS.computeIfAbsent(invocation.name(), name -> new LiteTestUser(name));
            return ContextResult.ok(() -> LiteTestUser);
        }

    }

    static class GuildContextual<S> implements ContextChainedProvider<S, LiteTestGuild> {

        @Override
        public ContextResult<LiteTestGuild> provide(Invocation<S> invocation, ContextChainAccessor<S> accessor) {
            return accessor.provideContext(LiteTestUser.class, invocation)
                .flatMap(user -> user.getGuild() == null
                    ? ContextResult.error("User is not in a guild")
                    : ContextResult.ok(() -> user.getGuild())
                );
        }

    }

    @Command(name = "command")
    static class TestCommand {

        @Execute(name = "user")
        void execute(@Context LiteTestUser user) {}

        @Execute(name = "guild get")
        void execute(@Context LiteTestGuild guild) {}

        @Execute(name = "guild set")
        void execute(@Context LiteTestUser user, @Arg String name) {
            user.setGuild(name);
        }
    }

    @Test
    @DisplayName("Should provide user context")
    void testUserContext() {
        platform.execute("command user")
            .assertSuccess();
    }

    @Test
    @DisplayName("Should provide guild context")
    void testGuildContext() {
        platform.execute("command guild get")
            .assertFailure();

        platform.execute("command guild set test")
            .assertSuccess();

        platform.execute("command guild get")
            .assertSuccess();
    }

}
