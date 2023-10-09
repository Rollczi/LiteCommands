package dev.rollczi.litecommands.message;

import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageRegistryTest {

    @Test
    void testGetMethodWhenValidKeyThenReturnCorrectMessage() {
        // Arrange
        MessageRegistry<TestSender> registry = new MessageRegistry<>();
        MessageKey<Void> key = MessageKey.of("validKey", "default message");
        String expectedMessage = "Hello, World!";
        registry.register(key, Message.of(o -> expectedMessage));

        // Act
        Optional<Object> actualMessage = registry.get(key);

        // Assert
        assertTrue(actualMessage.isPresent(), "Expected message to be present");
        assertEquals(expectedMessage, actualMessage.get(), "Expected message to be equal to the registered message");
    }

}