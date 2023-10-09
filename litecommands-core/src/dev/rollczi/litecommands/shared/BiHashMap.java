package dev.rollczi.litecommands.shared;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class BiHashMap<K1, K2, V> implements BiMap<K1, K2, V> {

    private final Map<K1, Map<K2, V>> nativeMap;

    public BiHashMap(BiMap<K1, K2, V> biMap) {
        this(biMap, biMap.flatSize());
    }

    public BiHashMap(BiMap<K1, K2, V> biMap, int initialCapacity) {
        this(initialCapacity);
        biMap.forEach((k1, k2, v) -> put(k1, k2, v));
    }

    public BiHashMap() {
        this(16);
    }

    public BiHashMap(int initialCapacity) {
        this.nativeMap = new HashMap<>(initialCapacity);
    }

    @Override
    public void put(K1 key1, K2 key2, V value) {
        Map<K2, V> map = nativeMap.computeIfAbsent(key1, k -> new HashMap<>());

        map.put(key2, value);
    }

    @Override
    public V get(K1 key1, K2 key2) {
        Map<K2, V> map = nativeMap.get(key1);

        if (map == null) {
            return null;
        }

        return map.get(key2);
    }

    @Override
    public V remove(K1 key1, K2 key2) {
        Map<K2, V> map = nativeMap.get(key1);

        if (map == null) {
            return null;
        }

        return map.remove(key2);
    }

    @Override
    public boolean containsKey(K1 key1, K2 key2) {
        Map<K2, V> map = nativeMap.get(key1);

        if (map == null) {
            return false;
        }

        return map.containsKey(key2);
    }

    @Override
    public void forEach(BiMapConsumer<K1, K2, V> consumer) {
        nativeMap.forEach((k1, map) -> map.forEach((k2, v) -> consumer.accept(k1, k2, v)));
    }

    @Override
    public int size() {
        return nativeMap.values().stream()
            .mapToInt(Map::size)
            .sum();
    }

    @Override
    public int flatSize() {
        return nativeMap.size();
    }

    @Override
    public V computeIfAbsent(K1 k1, K2 k2, BiFunction<K1, K2, V> function) {
        Map<K2, V> map = nativeMap.computeIfAbsent(k1, k -> new HashMap<>());

        return map.computeIfAbsent(k2, k -> function.apply(k1, k2));
    }

}
