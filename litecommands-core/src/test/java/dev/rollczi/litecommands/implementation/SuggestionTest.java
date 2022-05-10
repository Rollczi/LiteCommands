package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.command.SuggestResult;
import dev.rollczi.litecommands.command.FindResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import panda.std.Option;
import panda.std.Result;

import java.util.List;

class SuggestionTest {

    private final TestPlatform testPlatform = new TestPlatform();
    private final LiteCommands<Void> liteCommands = LiteFactory.builder(Void.class)
            .platform(testPlatform)
            .command(TestCommandLuckPermsExample.class)
            .command(TestCommandChatExample.class)
            .argument(String.class, (invocation, argument) -> Result.ok(argument))
            .register();


    @Test
    void checkSectionSuggestion() {
        FindResult findResult = testPlatform.find("lp", "user");
        SuggestResult result = SuggestResult.of(findResult.extractSuggestion());
        List<String> withFirst = result.suggestionWithFirst();

        Assertions.assertEquals(1, withFirst.size());
        Assertions.assertEquals("user", withFirst.get(0));
    }

    @Test
    void checkSectionSuggestion0() {
        FindResult findResult = testPlatform.find("lp", "use");
        SuggestResult result = SuggestResult.of(findResult.extractSuggestion());
        List<String> withFirst = result.suggestionWithFirst();

        Assertions.assertEquals(1, withFirst.size());
        Assertions.assertEquals("user", withFirst.get(0));
    }

    @Test
    void checkSectionSuggestion2() {
        FindResult findResult = testPlatform.find("lp", "user", "p");
        SuggestResult result = SuggestResult.of(findResult.extractSuggestion());
        List<String> withFirst = result.suggestionWithFirst();

        Assertions.assertEquals(1, withFirst.size());
        Assertions.assertEquals("parent", withFirst.get(0));
    }

    @Test
    void checkSectionSuggestion1() {
        FindResult findResult = testPlatform.find("lp");
        SuggestResult result = SuggestResult.of(findResult.extractSuggestion());
        List<String> withFirst = result.suggestionWithFirst();

        Assertions.assertEquals(1, withFirst.size());
        Assertions.assertTrue(withFirst.contains("lp"));
    }


}
