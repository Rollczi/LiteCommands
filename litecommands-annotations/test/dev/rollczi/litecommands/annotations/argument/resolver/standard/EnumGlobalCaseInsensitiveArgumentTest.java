package dev.rollczi.litecommands.annotations.argument.resolver.standard;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.argument.resolver.standard.EnumArgumentResolver;
import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import org.junit.jupiter.api.Test;

import static dev.rollczi.litecommands.reflect.type.TypeRange.upwards;

class EnumGlobalCaseInsensitiveArgumentTest extends LiteTestSpec {

    static LiteTestConfig config = builder -> builder.advanced()
        .argument(upwards(Enum.class), new EnumArgumentResolver<>(true));

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
    void testGlobalInsensitive() {
        platform.execute("test FIRST")
            .assertSuccess(TestEnum.FIRST);

        platform.execute("test first")
            .assertSuccess(TestEnum.FIRST);
    }

}
