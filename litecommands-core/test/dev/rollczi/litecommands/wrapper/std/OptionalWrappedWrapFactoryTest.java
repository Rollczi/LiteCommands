package dev.rollczi.litecommands.wrapper.std;

import dev.rollczi.litecommands.wrapper.Wrap;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class OptionalWrappedWrapFactoryTest {

    OptionalWrapper optionalFactory = new OptionalWrapper();

    @Test
    void wrap() {
        assertThrows(NullPointerException.class, () -> optionalFactory.create(null, null));
        assertThrows(NullPointerException.class, () -> optionalFactory.create(null, null));
        assertThrows(NullPointerException.class, () -> optionalFactory.create("value", null));

        assertThrows(IllegalArgumentException.class, () -> optionalFactory.create(null, WrapFormat.notWrapped(String.class)));
        assertThrows(IllegalArgumentException.class, () -> optionalFactory.create("value", WrapFormat.notWrapped(String.class)));

        assertThrows(IllegalArgumentException.class, () -> optionalFactory.create(null, WrapFormat.of(String.class, CompletableFuture.class)));
        assertThrows(IllegalArgumentException.class, () -> optionalFactory.create("value", WrapFormat.of(String.class, CompletableFuture.class)));

        Wrap<String> emptyWrap = optionalFactory.create(null, WrapFormat.of(String.class, Optional.class));
        Optional emptyOptional = assertInstanceOf(Optional.class, emptyWrap.unwrap());
        assertFalse(emptyOptional.isPresent());

        Wrap<String> wrap = optionalFactory.create("value", WrapFormat.of(String.class, Optional.class));
        Optional optional = assertInstanceOf(Optional.class, wrap.unwrap());
        assertTrue(optional.isPresent());
        assertEquals("value", optional.get());
    }

    @Test
    void empty() {
        assertThrows(NullPointerException.class, () -> optionalFactory.createEmpty(null));

        assertThrows(IllegalArgumentException.class, () -> optionalFactory.createEmpty(WrapFormat.notWrapped(String.class)));
        assertThrows(IllegalArgumentException.class, () -> optionalFactory.createEmpty(WrapFormat.of(String.class, CompletableFuture.class)));

        Wrap<String> emptyWrap = optionalFactory.createEmpty(WrapFormat.of(String.class, Optional.class));
        Optional emptyOptional = assertInstanceOf(Optional.class, emptyWrap.unwrap());
        assertFalse(emptyOptional.isPresent());
    }

    @Test
    void getWrapperType() {
        assertEquals(Optional.class, optionalFactory.getWrapperType());
    }

}