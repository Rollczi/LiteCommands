package dev.rollczi.litecommands.argument;

import java.util.Optional;

public interface ArgumentState {

    Argument<?> argument();

    Optional<String> name();

    Optional<String> scheme();

}
