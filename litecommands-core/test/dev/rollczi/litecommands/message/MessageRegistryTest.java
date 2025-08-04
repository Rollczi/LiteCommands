package dev.rollczi.litecommands.message;

import dev.rollczi.litecommands.unit.Invocations;
import dev.rollczi.litecommands.unit.TestSender;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import org.junit.jupiter.api.Test;

class MessageRegistryTest {

    @Test
    void testGetMethodWhenValidKeyThenReturnCorrectMessage() {
        MessageRegistry<TestSender> registry = new MessageRegistry<>();
        MessageKey<Void> key = MessageKey.of("validKey", "default message");
        registry.register(key, Message.of(o -> "Hello, World!"));

        assertMessage(registry, key, "Hello, World!");
    }

    @Test
    void testFallbackMechanism() {
        MessageRegistry<TestSender> registry = new MessageRegistry<>();
        MessageKey<Void> fallbackKey = MessageKey.of("fallbackKey", "default fallback message");
        MessageKey<Void> primaryKey = MessageKey.of("primaryKey", "default primary message", fallbackKey);

        assertMessage(registry, primaryKey, "default primary message");
        assertMessage(registry, fallbackKey, "default fallback message");

        registry.register(fallbackKey, Message.of(unused -> "Fallback message"));
        assertMessage(registry, primaryKey, "Fallback message");

        registry.register(primaryKey, Message.of(o -> "Primary message"));
        assertMessage(registry, primaryKey, "Primary message");
    }

    private static void assertMessage(MessageRegistry<TestSender> registry, MessageKey<Void> key, String message) {
        assertThat(registry.get(key, Invocations.empty()))
            .isEqualTo(message);
    }

}