package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.TestFactory;
import dev.rollczi.litecommands.TestPlatform;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.section.Section;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

class BigTypeTest {

    TestPlatform platform = TestFactory.withCommandsUniversalHandler(Command.class);

    @Section(route = "command")
    static class Command {
        @Execute(route = "int")
        BigInteger execute(@Arg BigInteger integer) {
            return integer;
        }

        @Execute(route = "decimal")
        BigDecimal execute(@Arg BigDecimal decimal) {
            return decimal;
        }
    }

    @Test
    void testBigInteger() {
        platform.execute("command", "int", "1000000")
                .assertSuccess()
                .assertResult(new BigInteger("1000000"));
    }

    @Test
    void testBigDecimal() {
        platform.execute("command", "decimal", "1000000.123")
                .assertSuccess()
                .assertResult(new BigDecimal("1000000.123"));
    }

}
