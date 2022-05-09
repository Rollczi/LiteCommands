package dev.rollczi.litecommands.implementation.scheme;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.command.FindResult;
import dev.rollczi.litecommands.implementation.LiteFactory;
import dev.rollczi.litecommands.implementation.TestPlatform;
import dev.rollczi.litecommands.scheme.SchemeFormat;
import dev.rollczi.litecommands.scheme.SchemeGenerator;
import org.junit.jupiter.api.Test;
import panda.std.Option;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SchemeGeneratorTest {

    private final TestPlatform testPlatform = new TestPlatform();
    private final LiteCommands<Void> liteCommands = LiteFactory.builder(Void.class)
            .platform(testPlatform)
            .command(SchemeGeneratorCommand.class)
            .resultHandler(String.class, (v, invocation, value) -> {})
            .optionalArgument(String.class, (invocation, argument) -> Option.of(argument))
            .register();

    @Test
    void test() {
        SchemeGenerator schemeGenerator = new SchemeGenerator();
        FindResult result = testPlatform.find("lp");

        String generate = schemeGenerator.generate(result, SchemeFormat.ARGUMENT_ANGLED_OPTIONAL_SQUARE);

        assertEquals("/lp", generate);
    }

}
