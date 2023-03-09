package dev.rollczi.litecommands.modern.test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Assert {

    public static <T> T assertPresent(Optional<T> optional) {
       assertTrue(optional.isPresent());
       return optional.get();
    }

}
