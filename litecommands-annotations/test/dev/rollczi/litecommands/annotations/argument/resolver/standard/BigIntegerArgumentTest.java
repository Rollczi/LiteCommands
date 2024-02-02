package dev.rollczi.litecommands.annotations.argument.resolver.standard;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

class BigIntegerArgumentTest extends LiteTestSpec {

    @Command(name = "test")
    static class TestCommand {

        @Execute
        BigInteger test(@Arg BigInteger testArgument) {
            return testArgument;
        }
    }

    @Test
    void test() {
        platform.execute("test 83737373")
            .assertSuccess(BigInteger.valueOf(83737373));

        platform.execute("test -88")
            .assertSuccess(BigInteger.valueOf(-88));

        platform.execute("test 0")
            .assertSuccess(BigInteger.valueOf(0));

        final BigInteger testBigInteger = BigInteger.ZERO
            .add(BigInteger.valueOf(Long.MAX_VALUE))
            .add(BigInteger.valueOf(Long.MAX_VALUE))
            .add(BigInteger.valueOf(Long.MAX_VALUE));

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