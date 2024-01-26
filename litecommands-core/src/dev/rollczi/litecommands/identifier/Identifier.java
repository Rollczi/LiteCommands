package dev.rollczi.litecommands.identifier;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public interface Identifier {

    Identifier CONSOLE = of(new UUID(0, 0), 0, 0L);

    <T> Optional<T> getIdentifier(Class<T> type);

    static Identifier of(Object... identifiers) {
        return new UniversalIdentifier(Arrays.asList(identifiers));
    }

}
