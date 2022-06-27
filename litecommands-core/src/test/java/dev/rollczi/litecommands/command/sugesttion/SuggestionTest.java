package dev.rollczi.litecommands.command.sugesttion;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.TestHandle;
import dev.rollczi.litecommands.TestPlatform;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.By;
import dev.rollczi.litecommands.argument.block.Block;
import dev.rollczi.litecommands.argument.flag.Flag;
import dev.rollczi.litecommands.argument.joiner.Joiner;
import dev.rollczi.litecommands.argument.simple.MultilevelArgument;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.argument.option.Opt;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.sugesstion.Suggestion;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.section.Section;
import dev.rollczi.litecommands.implementation.LiteFactory;
import org.junit.jupiter.api.Test;
import panda.std.Blank;
import panda.std.Option;
import panda.std.Result;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static dev.rollczi.litecommands.Assert.assertCollection;
import static dev.rollczi.litecommands.Assert.assertSize;
import static dev.rollczi.litecommands.TestUtils.list;
import static org.junit.jupiter.api.Assertions.*;

class SuggestionTest {

    private final TestPlatform testPlatform = new TestPlatform();
    private final LiteCommands<TestHandle> liteCommands = LiteFactory.builder(TestHandle.class)
            .platform(testPlatform)

            .command(SuggestionsCommand.class)
            .command(TeleportCommand.class)
            .command(LuckPermsCommand.class)
            .command(AdminChatCommand.class)

            .argument(String.class, new SuggestionArg("text"))
            .argument(String.class, "1", new SuggestionArg("arg-1.1", "arg-1.2", "arg-1.3"))
            .argument(String.class, "2", new SuggestionArg("arg-2.1", "arg-2.2", "arg-2.3"))
            .argument(String.class, "3", new SuggestionArg("arg-3.1", "arg-3.2", "arg-3.3"))
            .argumentMultilevel(Location.class, new SuggestionLocationArg())

            .register();


    @Test
    void normalSuggestion() {
        List<String> suggestion = testPlatform.suggestion("lp", "user");

        assertEquals(1, suggestion.size());
        assertEquals("user", suggestion.get(0));
    }

    @Test
    void notFullSuggestion() {
        List<String> suggestion = testPlatform.suggestion("lp", "use");

        assertEquals(1, suggestion.size());
        assertEquals("user", suggestion.get(0));
    }

    @Test
    void firstBlockSuggestion() {
        List<String> suggestion = testPlatform.suggestion("lp", "user", "text", "p");

        assertCollection(2, list("parent set", "parent unset"), suggestion);
    }

    @Test
    void firsEmptyBlockSuggestion() {
        List<String> suggestion = testPlatform.suggestion("lp", "user", "text", "");

        assertCollection(3, list("parent set", "parent unset", "reload"), suggestion);
    }


    @Test
    void secondBlockSuggestion() {
        List<String> suggestion = testPlatform.suggestion("lp", "user", "text", "parent", "");

        assertCollection(2, list("set", "unset"), suggestion);
    }

    @Test
    void afterBlockSuggestion() {
        List<String> suggestion = testPlatform.suggestion("lp", "user", "text", "parent", "set", "");

        assertEquals(1, suggestion.size());
        assertEquals("text", suggestion.get(0));
    }

    @Test
    void aliasesSuggestion() {
        List<String> suggestion = testPlatform.suggestion("lp");

        assertEquals(2, suggestion.size());
        assertEquals("luckperms", suggestion.get(0));
        assertEquals("lp", suggestion.get(1));
    }

    @Test
    void manySuggestionsTest() {
        List<String> suggestion = testPlatform.suggestion("suggestions-test", "execute-1", "");

        assertEquals(3, suggestion.size());
        assertEquals("arg-1.1", suggestion.get(0));
        assertEquals("arg-1.2", suggestion.get(1));
        assertEquals("arg-1.3", suggestion.get(2));
    }

    @Test
    void suggestionsWithOptionalArgumentTest() {
        List<String> suggestion = testPlatform.suggestion("suggestions-test", "execute-2", "arg-1.1", "arg-");

        assertEquals(3, suggestion.size());
        assertEquals("arg-2.1", suggestion.get(0));
        assertEquals("arg-2.2", suggestion.get(1));
        assertEquals("arg-2.3", suggestion.get(2));
    }

    @Test
    void suggestionsWithOptionalArgumentsTest() {
        List<String> suggestion = testPlatform.suggestion("suggestions-test", "execute-3", "arg");

        assertSize(9, suggestion);
        assertCollection(list("arg-1.1", "arg-1.2", "arg-1.3"), suggestion);
        assertCollection(list("arg-3.1", "arg-3.2", "arg-3.3"), suggestion);
    }

    @Test
    void suggestionsWithTwoOptionalArgumentsTest() {
        List<String> suggestion = testPlatform.suggestion("suggestions-test", "execute-3", "arg-1.1", "arg-");

        assertSize(6, suggestion);
        assertCollection(list("arg-2.1", "arg-2.2", "arg-2.3"), suggestion);
        assertCollection(list("arg-3.1", "arg-3.2", "arg-3.3"), suggestion);
    }

    @Test
    void multilevelEmptyArgumentTest() {
        List<String> suggestion = testPlatform.suggestion("teleport", "");

        assertCollection(2, list("text", "100 100 100"), suggestion);
    }

    @Test
    void multilevelOneArgumentTest() {
        List<String> suggestion = testPlatform.suggestion("teleport", "1");

        assertEquals(1, suggestion.size());
        assertEquals("100 100 100", suggestion.get(0));
    }

    @Test
    void
    nextToMultilevelExecuteTest() {
        List<String> suggestion = testPlatform.suggestion("teleport", "Rollczi", "");

        assertEquals(0, suggestion.size());
    }

    @Test
    void allArgumentsMultilevelTest() {
        List<String> suggestion = testPlatform.suggestion("teleport", "100", "100", "100", "");

        assertEquals(1, suggestion.size());
        assertEquals("text", suggestion.get(0));
    }

    @Section(route = "suggestions-test")
    static class SuggestionsCommand {
        @Execute(route = "execute-1") void execute1(@Arg @By("1") String a1, @Arg @By("2") String a2, @Arg @By("3") String a3) {}
        @Execute(route = "execute-2") void execute2(@Arg @By("1") String a1, @Arg @By("2") String a2, @Opt @By("3") Option<String> a3) {}
        @Execute(route = "execute-3") void execute3(@Opt @By("1") Option<String> a1, @Opt @By("2") Option<String> a2, @Arg @By("3") String a3) {}
    }

    @Section(route = "teleport")
    static class TeleportCommand {
        @Execute(min = 3, max = 4) void execute1(@Arg Location location, @Opt Option<String> world) {}
        @Execute(required = 1) void execute2(@Arg String player) {}
    }

    @Section(route = "lp user", aliases = "luckperms user")
    static class LuckPermsCommand {
        @Execute String set(@Arg String user, @Block("parent set") @Arg String rank) { return user + " -> " + rank; }
        @Execute String unset(@Arg String user, @Block("parent unset") @Arg String rank) { return user + " -x " + rank;}
        @Execute String reload(@Arg String user, @Block("reload") Blank none) { return user + " -x ";}
    }

    @Section(route = "ac", aliases = "adminchat")
    static class AdminChatCommand {
        @Execute String unset(@Flag("-s") boolean silent, @Joiner String text) { return silent + " -> " + text; }
        @Execute(route = "key") String unset(@Opt Option<String> first) { return first.isPresent() ? first.get() : "null"; }
    }

    static class SuggestionArg implements OneArgument<String> {
        private final List<String> suggestions;

        SuggestionArg(String... suggest) { this.suggestions = Arrays.asList(suggest); }

        @Override
        public Result<String, Object> parse(LiteInvocation invocation, String argument) { return Result.ok(argument); }

        @Override
        public List<Suggestion> suggest(LiteInvocation invocation) {
            return this.suggestions.stream().map(Suggestion::of).collect(Collectors.toList());
        }
    }

    static class Location {
        final double x, y, z;

        Location(double x, double y, double z) {
            this.x = x; this.y = y; this.z = z;
        }
    }

    static class SuggestionLocationArg implements MultilevelArgument<Location> {
        @Override
        public Result<Location, ?> parseMultilevel(LiteInvocation invocation, String... arguments) {
            return Result.attempt(NumberFormatException.class, () -> new Location(Double.parseDouble(arguments[0]), Double.parseDouble(arguments[1]), Double.parseDouble(arguments[2])));
        }

        @Override
        public List<Suggestion> suggest(LiteInvocation invocation) {
            return Collections.singletonList(Suggestion.multilevel("100", "100", "100"));
        }

        @Override
        public int countMultilevel() { return 3; }
    }

}
