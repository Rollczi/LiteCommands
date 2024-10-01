package dev.rollczi.litecommands.argument;

import java.util.Objects;

class ArgumentKeyImpl implements ArgumentKey {

    private final String namespace;
    private final String key;

    ArgumentKeyImpl(String namespace, String key) {
        this.namespace = namespace;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public ArgumentKeyImpl withKey(String key) {
        return new ArgumentKeyImpl(this.namespace, key);
    }

    public String getNamespace() {
        return namespace;
    }

    public ArgumentKeyImpl withNamespace(String namespace) {
        return new ArgumentKeyImpl(namespace, this.key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ArgumentKeyImpl that = (ArgumentKeyImpl) o;
        return namespace.equals(that.namespace) && key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, key);
    }

    @Override
    public String toString() {
        return namespace + ":" + key;
    }

}
