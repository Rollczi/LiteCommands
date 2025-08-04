package dev.rollczi.litecommands.adventure;

import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.requirement.RequirementResult;
import dev.rollczi.litecommands.unit.Arguments;
import dev.rollczi.litecommands.unit.Invocations;
import dev.rollczi.litecommands.unit.TestSender;
import net.kyori.adventure.key.Key;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class AdventureKeyArgumentTest {

    static MessageRegistry<TestSender> messages = new MessageRegistry<>();
    static AdventureKeyArgument<TestSender> argument = new AdventureKeyArgument<>(messages);

    @BeforeAll
    static void before() {
        messages.register(LiteAdventureMessages.ADVENTURE_KEY_INVALID, invalidKey -> "Invalid key: " + invalidKey);
    }

    @Test
    void shouldParseValidKey() {
        assertThat(parse("name:key").asString())
            .isEqualTo("name:key");

        assertThat(parse(":key").asString())
            .isEqualTo("minecraft:key");

        assertThat(parse(":").asString())
            .isEqualTo("minecraft:");

        assertThat(parse("").asString())
            .isEqualTo("minecraft:");
    }

    @Test
    void shouldFailWithInvalidKey() {
        assertThat(tryParse("34a&^:#@)@#)").getFailedReason())
            .isEqualTo("Invalid key: 34a&^:#@)@#)");
    }

    private static Key parse(String... input) {
        return tryParse(input)
            .getSuccess();
    }

    private static RequirementResult<Key> tryParse(String... input) {
        return argument.parse(Invocations.invocation("result", input), Arguments.argument(), RawInput.of(input))
            .getNow(ParseResult.failure("EMPTY"));
    }

}
