package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.argument.profile.ArgumentProfileKey;

public interface ArgumentKey {

    String DEFAULT_KEY = "";
    String DEFAULT_NAMESPACE = Argument.class.getName();

    ArgumentKey DEFAULT = ArgumentKey.of(DEFAULT_NAMESPACE, DEFAULT_KEY);

    @Deprecated
    String UNIVERSAL_NAMESPACE = DEFAULT_NAMESPACE;
    @Deprecated
    ArgumentKey DEFAULT_UNIVERSAL = DEFAULT;

    String getKey();

    String getNamespace();

    ArgumentKey withKey(String key);

    ArgumentKey withNamespace(String namespace);

    @Deprecated
    default  <A extends Argument<?>> ArgumentKey withNamespace(Class<A> argumentType) {
        return withNamespace(argumentType.getName());
    }

    default ArgumentKey withDefaultKey() {
        return withKey(DEFAULT_KEY);
    }

    default ArgumentKey withDefaultNamespace() {
        return withNamespace(DEFAULT_NAMESPACE);
    }

    default boolean isDefaultKey() {
        return getKey().isEmpty();
    }

    default boolean isDefaultNamespace() {
        return getNamespace().equals(DEFAULT_NAMESPACE);
    }

    @Deprecated
    default boolean isDefault() {
        return this.isDefaultKey();
    }

    @Deprecated
    default boolean isUniversal() {
        return this.isDefaultNamespace();
    }


    static ArgumentKey of() {
        return DEFAULT;
    }

    static ArgumentKey of(String key) {
        return of(DEFAULT_NAMESPACE, key);
    }

    static ArgumentKey of(String namespace, String key) {
        return new ArgumentKeyImpl(namespace, key);
    }

    @Deprecated
    static <A extends Argument<?>> ArgumentKey typed(Class<A> argumentType, String key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        return new ArgumentKeyImpl(argumentType.getName(), key);
    }

    @Deprecated
    static <A extends Argument<?>> ArgumentKey typed(Class<A> argumentType) {
        return typed(argumentType, "");
    }

    default ArgumentKey profiled(ArgumentProfileKey<?> key) {
        return key.withKey(getKey());
    }

}
