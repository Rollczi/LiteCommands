package dev.rollczi.litecommands.annotations.parser;

import dev.rollczi.litecommands.annotations.LiteConfig;
import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.test.LiteTestGuild;
import dev.rollczi.litecommands.annotations.test.LiteTestUser;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.ParserChainAccessor;
import dev.rollczi.litecommands.argument.parser.ParserChained;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ParserTest extends LiteTestSpec {

    static LiteConfig config = builder -> builder
        .argumentParser(LiteTestUser.class, new UserParser<>())
        .argumentParser(LiteTestGuild.class, new GuildParser<>());

    static Map<String, LiteTestUser> USERS = new ConcurrentHashMap<>();

    static class UserParser<S> implements Parser<S, LiteTestUser> {

        @Override
        public ParseResult<LiteTestUser> parse(Invocation<S> invocation, Argument<LiteTestUser> argument, RawInput input) {
            return ParseResult.success(USERS.computeIfAbsent(input.next(), name -> new LiteTestUser(name)));
        }

        @Override
        public Range getRange(Argument<LiteTestUser> userArgument) {
            return Range.ONE;
        }

    }

    static class GuildParser<S> implements ParserChained<S, LiteTestGuild> {

        @Override
        public ParseResult<LiteTestGuild> parse(Invocation<S> invocation, Argument<LiteTestGuild> argument, RawInput input, ParserChainAccessor<S> chainAccessor) {
            return chainAccessor.parse(invocation, Argument.of(argument, LiteTestUser.class), input)
                .flatMap(user -> user.getGuild() == null
                    ? ParseResult.failure("User is not in a guild")
                    : ParseResult.success(user.getGuild())
                );
        }

        @Override
        public Range getRange(Argument<LiteTestGuild> guildArgument) {
            return Range.ONE;
        }

    }

    @Command(name = "command")
    static class TestCommand {

        @Execute(name = "user")
        void execute(@Arg("user") LiteTestUser user) {}

        @Execute(name = "guild get")
        void execute(@Arg LiteTestGuild guild) {}

        @Execute(name = "guild set")
        void execute(@Arg LiteTestUser user, @Arg String guildName) {
            user.setGuild(guildName);
        }

    }

    @Test
    @DisplayName("Should parse user")
    void testUserParser() {
        platform.execute("command user test-user")
            .assertSuccess();
    }

    @Test
    @DisplayName("Should parse guild")
    void testGuildParser() {
        platform.execute("command guild get test-user")
            .assertFailure();

        platform.execute("command guild set test-user test-guild")
            .assertSuccess();

        platform.execute("command guild get test-user")
            .assertSuccess();
    }

}
