package dev.rollczi.litecommands.programmatic;

import dev.rollczi.litecommands.unit.LiteCommandsTestFactory;
import dev.rollczi.litecommands.unit.TestPlatform;
import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AliasesTest {

    TestPlatform testPlatform = LiteCommandsTestFactory.startPlatform(builder -> builder.commands(
        new LiteCommand<TestSender>("main", "main-alias1", "main-alias2")
            .executeReturn(context -> context.invocation().label())
    ));

    @Test
    @DisplayName("Should execute command with alias")
    public void testSuccess() {
        testPlatform.execute("main")
            .assertSuccess("main")
            .assertMessage("main");

        testPlatform.execute("main-alias1")
            .assertSuccess("main-alias1")
            .assertMessage("main-alias1");

        testPlatform.execute("main-alias2")
            .assertSuccess("main-alias2")
            .assertMessage("main-alias2");
    }

    @Test
    @DisplayName("Should suggest aliases")
    public void testSuggest() {
        testPlatform.suggest("main")
            .assertSuggest("main", "main-alias1", "main-alias2");
    }

}
