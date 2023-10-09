package dev.rollczi.litecommands.shared;

@FunctionalInterface
public interface BiMapConsumer<K1, K2, V> {

    void accept(K1 key1, K2 key2, V value);

}
