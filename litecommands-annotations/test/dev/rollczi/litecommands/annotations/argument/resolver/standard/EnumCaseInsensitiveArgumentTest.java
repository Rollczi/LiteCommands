package dev.rollczi.litecommands.annotations.argument.resolver.standard;

import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.argument.CaseInsensitive;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.junit.jupiter.api.Test;

class EnumCaseInsensitiveArgumentTest extends LiteTestSpec {

    @Command(name = "test")
    static class TestCommand {

        @Execute(name = "sensitive")
        TestEnum sensitive(@Arg TestEnum testEnum) {
            return testEnum;
        }

        @Execute(name = "insensitive")
        TestEnum insensitive(@Arg @CaseInsensitive TestEnum testEnum) {
            return testEnum;
        }

    }

    enum TestEnum {
        FIRST,
        SECOND,
    }

    @Test
    void testSensitive() {
        platform.execute("test sensitive FIRST")
            .assertSuccess(TestEnum.FIRST);

        platform.execute("test sensitive first")
            .assertFailure();
    }

    @Test
    void testInsensitive() {
        platform.execute("test insensitive FIRST")
            .assertSuccess(TestEnum.FIRST);

        platform.execute("test insensitive first")
            .assertSuccess(TestEnum.FIRST);

        platform.execute("test insensitive First")
            .assertSuccess(TestEnum.FIRST);

        platform.execute("test insensitive SECOND")
            .assertSuccess(TestEnum.SECOND);

        platform.execute("test insensitive second")
            .assertSuccess(TestEnum.SECOND);
    }

    @Test
    void testInsensitiveSuggestions() {
        platform.suggest("test insensitive ")
            .assertSuggest("FIRST", "SECOND");

        platform.suggest("test insensitive f")
            .assertSuggest("FIRST");

        platform.suggest("test insensitive F")
            .assertSuggest("FIRST");
    }

}
