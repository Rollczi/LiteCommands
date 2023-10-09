package dev.rollczi.litecommands.shared;

import java.util.function.BiFunction;

public interface BiMap<K1, K2, V> {

    void put(K1 key1, K2 key2, V value);

    V get(K1 key1, K2 key2);

    V remove(K1 key1, K2 key2);

    boolean containsKey(K1 key1, K2 key2);

    void forEach(BiMapConsumer<K1, K2, V> consumer);

    int size();

    int flatSize();

    V computeIfAbsent(K1 k1, K2 k2, BiFunction<K1, K2, V> function);

}
