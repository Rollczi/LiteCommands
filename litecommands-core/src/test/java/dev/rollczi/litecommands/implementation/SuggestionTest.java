package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.By;
import dev.rollczi.litecommands.argument.one.OneArgument;
import dev.rollczi.litecommands.argument.option.Opt;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.sugesstion.Suggestion;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.section.Section;
import org.junit.jupiter.api.Test;
import panda.std.Option;
import panda.std.Result;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SuggestionTest {

    private final TestPlatform testPlatform = new TestPlatform();
    private final LiteCommands<Void> liteCommands = LiteFactory.builder(Void.class)
            .platform(testPlatform)
            .command(SuggestionsTest.class)
            .command(TestCommandLuckPermsExample.class)
            .command(TestCommandChatExample.class)
            .argument(String.class, (invocation, argument) -> Result.ok(argument))
            .argument(String.class, "1", new SuggestionArg("arg-1.1", "arg-1.2", "arg-1.3"))
            .argument(String.class, "2", new SuggestionArg("arg-2.1", "arg-2.2", "arg-2.3"))
            .argument(String.class, "3", new SuggestionArg("arg-3.1", "arg-3.2", "arg-3.3"))
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
        List<String> suggestion = testPlatform.suggestion("lp", "user", "Rollczi", "p");

        assertEquals(1, suggestion.size());
        assertEquals("parent", suggestion.get(0));
    }

    @Test
    void secondBlockSuggestion() {
        List<String> suggestion = testPlatform.suggestion("lp", "user", "Rollczi", "parent", "");

        assertEquals(1, suggestion.size());
        assertEquals("set", suggestion.get(0));
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
        List<String> suggestion = testPlatform.suggestion("suggestions-test", "execute-2", "arg1", "arg2");

        assertEquals(3, suggestion.size());
        assertEquals("arg-2.1", suggestion.get(0));
        assertEquals("arg-2.2", suggestion.get(1));
        assertEquals("arg-2.3", suggestion.get(2));
    }

    @Test
    void suggestionsWithOptionalArgumentsTest() {
        List<String> suggestion = testPlatform.suggestion("suggestions-test", "execute-3", "arg1");

        assertEquals(9, suggestion.size());
        assertEquals("arg-1.1", suggestion.get(0));
        assertEquals("arg-1.2", suggestion.get(1));
        assertEquals("arg-1.3", suggestion.get(2));
        assertEquals("arg-2.1", suggestion.get(3));
        assertEquals("arg-2.2", suggestion.get(4));
        assertEquals("arg-2.3", suggestion.get(5));
        assertEquals("arg-3.1", suggestion.get(6));
        assertEquals("arg-3.2", suggestion.get(7));
        assertEquals("arg-3.3", suggestion.get(8));
    }

    @Test
    void multilevelSuggestionsWithOptionalArgumentsTest() {
        List<String> suggestion = testPlatform.suggestion("suggestions-test", "execute-3", "arg1", "arg2");

        assertEquals(6, suggestion.size());
        assertEquals("arg-2.1", suggestion.get(0));
        assertEquals("arg-2.2", suggestion.get(1));
        assertEquals("arg-2.3", suggestion.get(2));
        assertEquals("arg-3.1", suggestion.get(3));
        assertEquals("arg-3.2", suggestion.get(4));
        assertEquals("arg-3.3", suggestion.get(5));
    }

    @Section(route = "suggestions-test")
    static class SuggestionsTest {
        @Execute(route = "execute-1")
        void execute1(@Arg @By("1") String a1, @Arg @By("2") String a2, @Arg @By("3") String a3) {}
        @Execute(route = "execute-2")
        void execute2(@Arg @By("1") String a1, @Arg @By("2") String a2, @Opt @By("3") Option<String> a3) {}
        @Execute(route = "execute-3")
        void execute3(@Opt @By("1") Option<String> a1, @Opt @By("2") Option<String> a2, @Arg @By("3") String a3) {}
    }

    static class SuggestionArg implements OneArgument<String> {

        private final List<String> suggestions;

        SuggestionArg(String... suggest) {
            this.suggestions = Arrays.asList(suggest);
        }

        @Override
        public Result<String, Object> parse(LiteInvocation invocation, String argument) {
            return Result.ok(argument);
        }

        @Override
        public List<Suggestion> suggest(LiteInvocation invocation) {
            return this.suggestions.stream()
                    .map(Suggestion::of)
                    .collect(Collectors.toList());
        }

    }

}
