package dev.rollczi.litecommands.identifier;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

class UniversalIdentifier implements Identifier {

    private final List<Object> identifiers;

    UniversalIdentifier(List<Object> identifiers) {
        this.identifiers = identifiers;
    }

    @Override
    public <T> Optional<T> getIdentifier(Class<T> type) {
        return identifiers.stream()
            .filter(obj -> type.isInstance(obj))
            .map(obj -> type.cast(obj))
            .findFirst();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UniversalIdentifier that = (UniversalIdentifier) o;
        return Objects.equals(identifiers, that.identifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifiers);
    }

}
