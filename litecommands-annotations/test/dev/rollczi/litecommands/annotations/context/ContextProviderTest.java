package dev.rollczi.litecommands.annotations.context;

import dev.rollczi.litecommands.annotations.LiteConfig;
import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.context.ContextChainAccessor;
import dev.rollczi.litecommands.context.ContextChainedProvider;
import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ContextProviderTest extends LiteTestSpec {

    static LiteConfig config = builder -> builder
        .context(User.class, new UserContextual<>())
        .context(Guild.class, new GuildContextual<>())
        .context(Integer.class, new IntegerContextual<>())
        .context(Double.class, new DoubleContextual<>());

    static Map<String, User> USERS = new ConcurrentHashMap<>();

    static class User {

        private final String name;
        private Guild guild;

        User(String name, @Nullable Guild guild) {
            this.name = name;
        }

        User(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public @Nullable Guild getGuild() {
            return this.guild;
        }

        public void setGuild(Guild guild) {
            this.guild = guild;
        }

    }

    static class Guild {

        private final String name;

        Guild(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

    }

    static class UserContextual<S> implements ContextProvider<S, User> {

        @Override
        public ContextResult<User> provide(Invocation<S> invocation) {
            User user = USERS.computeIfAbsent(invocation.name(), name -> new User(name));
            return ContextResult.ok(() -> user);
        }

    }

    static class GuildContextual<S> implements ContextChainedProvider<S, Guild> {

        @Override
        public ContextResult<Guild> provide(Invocation<S> invocation, ContextChainAccessor<S> accessor) {
            ContextResult<User> userContext = accessor.provideContext(User.class, invocation);
            if (userContext.isFailed()) {
                return ContextResult.error(userContext);
            }
            User user = userContext.getSuccess();

            Guild guild = user.getGuild();
            if (guild == null) {
                return ContextResult.error("User is not in a guild");
            }
            return ContextResult.ok(() -> guild);
        }

    }

    static class IntegerContextual<S> implements ContextChainedProvider<S, Integer> {

        @Override
        public ContextResult<Integer> provide(Invocation<S> invocation, ContextChainAccessor<S> accessor) {
            ContextResult<Double> doubleContext = accessor.provideContext(Double.class, invocation);
            if (doubleContext.isFailed()) {
                return ContextResult.error(doubleContext);
            }
            return ContextResult.ok(() -> doubleContext.getSuccess().intValue());
        }

    }

    static class DoubleContextual<S> implements ContextProvider<S, Double> {

        @Override
        public ContextResult<Double> provide(Invocation<S> invocation) {
            return ContextResult.error("Double always fails");
        }

    }

    @Command(name = "command")
    static class TestCommand {

        @Execute(name = "user")
        void execute(@Context User user) {}

        @Execute(name = "guild get")
        void execute(@Context Guild guild) {}

        @Execute(name = "guild set")
        void execute(@Context User user, @Arg String guildName) {
            user.setGuild(new Guild(guildName));
            USERS.put(user.getName(), user);
        }

        @Execute(name = "failed")
        void execute(@Context Integer integer) {}

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

    @Test
    @DisplayName("Should fail providing context")
    void testFailedContext() {
        platform.execute("command failed")
            .assertFailure("Double always fails");
    }


}
