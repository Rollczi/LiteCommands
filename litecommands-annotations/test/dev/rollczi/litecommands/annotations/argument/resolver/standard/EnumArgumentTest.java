package dev.rollczi.litecommands.annotations.argument.resolver.standard;

import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.junit.jupiter.api.Test;

class EnumArgumentTest extends LiteTestSpec {

    @Command(name = "test")
    static class TestCommand {

        @Execute
        TestEnum test(@Arg TestEnum testEnum) {
            return testEnum;
        }
    }

    enum TestEnum {
        FIRST,
        SECOND,
    }

    @Test
    void test() {
        platform.execute("test FIRST")
            .assertSuccess(TestEnum.FIRST);

        platform.execute("test SECOND")
            .assertSuccess(TestEnum.SECOND);
    }

    @Test
    void testInvalid() {
        platform.execute("test invalid")
            .assertFailure();
    }

    @Test
    void testSuggestions() {
        platform.suggest("test ")
            .assertSuggest("FIRST", "SECOND");

        platform.suggest("test F")
            .assertSuggest("FIRST");

        // test cached suggestions (test mutable state)
        platform.suggest("test ")
            .assertSuggest("FIRST", "SECOND");
    }

}