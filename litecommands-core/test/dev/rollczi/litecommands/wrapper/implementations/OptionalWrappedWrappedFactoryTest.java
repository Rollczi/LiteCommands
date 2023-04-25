package dev.rollczi.litecommands.wrapper.implementations;

import dev.rollczi.litecommands.wrapper.Wrapped;
import dev.rollczi.litecommands.wrapper.WrapperFormat;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class OptionalWrappedWrappedFactoryTest {

    OptionalWrappedExpectedFactory optionalFactory = new OptionalWrappedExpectedFactory();

    @Test
    void wrap() {
        assertThrows(NullPointerException.class, () -> optionalFactory.create(null, null));
        assertThrows(NullPointerException.class, () -> optionalFactory.create(() -> null, null));
        assertThrows(NullPointerException.class, () -> optionalFactory.create(() -> "value", null));

        assertThrows(IllegalArgumentException.class, () -> optionalFactory.create(() -> null, WrapperFormat.notWrapped(String.class)));
        assertThrows(IllegalArgumentException.class, () -> optionalFactory.create(() -> "value", WrapperFormat.notWrapped(String.class)));

        assertThrows(IllegalArgumentException.class, () -> optionalFactory.create(() -> null, WrapperFormat.of(String.class, CompletableFuture.class)));
        assertThrows(IllegalArgumentException.class, () -> optionalFactory.create(() -> "value", WrapperFormat.of(String.class, CompletableFuture.class)));

        Wrapped<String> emptyWrap = optionalFactory.create(() -> null, WrapperFormat.of(String.class, Optional.class));
        Optional emptyOptional = assertInstanceOf(Optional.class, emptyWrap.unwrap());
        assertFalse(emptyOptional.isPresent());

        Wrapped<String> wrap = optionalFactory.create(() -> "value", WrapperFormat.of(String.class, Optional.class));
        Optional optional = assertInstanceOf(Optional.class, wrap.unwrap());
        assertTrue(optional.isPresent());
        assertEquals("value", optional.get());
    }

    @Test
    void empty() {
        assertThrows(NullPointerException.class, () -> optionalFactory.createEmpty(null));

        assertThrows(IllegalArgumentException.class, () -> optionalFactory.createEmpty(WrapperFormat.notWrapped(String.class)));
        assertThrows(IllegalArgumentException.class, () -> optionalFactory.createEmpty(WrapperFormat.of(String.class, CompletableFuture.class)));

        Wrapped<String> emptyWrap = optionalFactory.createEmpty(WrapperFormat.of(String.class, Optional.class));
        Optional emptyOptional = assertInstanceOf(Optional.class, emptyWrap.unwrap());
        assertFalse(emptyOptional.isPresent());
    }

    @Test
    void getWrapperType() {
        assertEquals(Optional.class, optionalFactory.getWrapperType());
    }

}