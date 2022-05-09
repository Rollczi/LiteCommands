package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.command.CompletionResult;
import dev.rollczi.litecommands.command.FindResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import panda.std.Option;

import java.util.List;

class CompletionTest {

    private final TestPlatform testPlatform = new TestPlatform();
    private final LiteCommands<Void> liteCommands = LiteFactory.builder(Void.class)
            .platform(testPlatform)
            .command(TestCommandLuckPermsExample.class)
            .command(TestCommandChatExample.class)
            .optionalArgument(String.class, (invocation, argument) -> Option.of(argument))
            .register();


    @Test
    void checkSectionCompletion() {
        FindResult findResult = testPlatform.find("lp", "user");
        CompletionResult result = CompletionResult.of(findResult.extractCompletion());
        List<String> withFirst = result.completionsWithFirst();

        Assertions.assertEquals(1, withFirst.size());
        Assertions.assertEquals("user", withFirst.get(0));
    }

    @Test
    void checkSectionCompletion0() {
        FindResult findResult = testPlatform.find("lp", "use");
        CompletionResult result = CompletionResult.of(findResult.extractCompletion());
        List<String> withFirst = result.completionsWithFirst();

        Assertions.assertEquals(1, withFirst.size());
        Assertions.assertEquals("user", withFirst.get(0));
    }

    @Test
    void checkSectionCompletion2() {
        FindResult findResult = testPlatform.find("lp", "user", "p");
        CompletionResult result = CompletionResult.of(findResult.extractCompletion());
        List<String> withFirst = result.completionsWithFirst();

        Assertions.assertEquals(1, withFirst.size());
        Assertions.assertEquals("parent", withFirst.get(0));
    }

    @Test
    void checkSectionCompletion1() {
        FindResult findResult = testPlatform.find("lp");
        CompletionResult result = CompletionResult.of(findResult.extractCompletion());
        List<String> withFirst = result.completionsWithFirst();

        Assertions.assertEquals(1, withFirst.size());
        Assertions.assertTrue(withFirst.contains("lp"));
    }


}
