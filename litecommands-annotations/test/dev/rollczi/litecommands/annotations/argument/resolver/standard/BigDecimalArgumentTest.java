package dev.rollczi.litecommands.annotations.argument.resolver.standard;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigDecimalArgumentTest extends LiteTestSpec {

    @Command(name = "test")
    static class TestCommand {

        @Execute
        BigDecimal test(@Arg BigDecimal testArgument) {
            return testArgument;
        }
    }

    @Test
    void test() {
        platform.execute("test 27.83")
            .assertSuccess(BigDecimal.valueOf(27.83));

        platform.execute("test -88.3873729363")
            .assertSuccess(BigDecimal.valueOf(-88.3873729363));

        platform.execute("test 14")
            .assertSuccess(BigDecimal.valueOf(14));

        platform.execute("test 0")
            .assertSuccess(BigDecimal.valueOf(0));

        final BigDecimal testBigInteger = BigDecimal.ZERO
            .add(BigDecimal.valueOf(Double.MAX_VALUE))
            .add(BigDecimal.valueOf(Double.MAX_VALUE))
            .add(BigDecimal.valueOf(Double.MAX_VALUE));

        platform.execute("test " + testBigInteger)
            .assertSuccess(testBigInteger);
    }

    @Test
    void testInvalid() {
        platform.execute("test invalid")
            .assertFailure();
    }

    @Test
    void testSuggestions() {
        platform.suggest("test ")
            .assertNotEmpty()
            .assertCorrect(suggestion -> platform.execute("test " + suggestion.multilevel()).assertSuccess());
    }
}