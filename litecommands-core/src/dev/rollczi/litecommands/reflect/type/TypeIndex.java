package dev.rollczi.litecommands.reflect.type;

import dev.rollczi.litecommands.reflect.ReflectUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TypeIndex<T> {

    private final Map<Class<?>, T> sameIndex = new HashMap<>();
    private final Map<Class<?>, T> downwardIndex = new HashMap<>();
    private final Map<Class<?>, T> upwardIndex = new HashMap<>();

    public void put(TypeRange<?> range, T value) {
        if (range instanceof TypeRange.SameTypeRange) {
            TypeRange.SameTypeRange<?> sameTypeRange = (TypeRange.SameTypeRange<?>) range;
            sameIndex.put(sameTypeRange.getSame(), value);
            return;
        }

        if (range instanceof TypeRange.DownwardsTypeRange) {
            TypeRange.DownwardsTypeRange<?> downwardsTypeRange = (TypeRange.DownwardsTypeRange<?>) range;

            for (Class<?> aClass : downwardsTypeRange.getInclude()) {
                downwardIndex.put(aClass, value);
            }

            return;
        }

        if (range instanceof TypeRange.UpwardsTypeRange) {
            TypeRange.UpwardsTypeRange<?> upwardsTypeRange = (TypeRange.UpwardsTypeRange<?>) range;
            upwardIndex.put(upwardsTypeRange.getType(), value);
        }
    }

    public List<T> get(Class<?> type) {
        List<T> values = new ArrayList<>();

        T value = sameIndex.get(type);

        if (value != null) {
            values.add(value);
        }

        T t = downwardIndex.get(type);

        if (t != null) {
            values.add(t);
        }

        for (Class<?> allType : ReflectUtil.getAllTypes(type)) {
            T t1 = upwardIndex.get(allType);

            if (t1 != null) {
                values.add(t1);
            }
        }

        T object = upwardIndex.get(Object.class);

        if (object != null) {
            values.add(object);
        }

        return values;
    }

    public List<T> computeIfAbsent(TypeRange<?> range, Supplier<T> supplier) {
        if (range instanceof TypeRange.SameTypeRange) {
            TypeRange.SameTypeRange<?> sameTypeRange = (TypeRange.SameTypeRange<?>) range;
            T value = sameIndex.computeIfAbsent(sameTypeRange.getSame(), k -> supplier.get());

            return Collections.singletonList(value);
        }

        if (range instanceof TypeRange.DownwardsTypeRange) {
            TypeRange.DownwardsTypeRange<?> downwardsTypeRange = (TypeRange.DownwardsTypeRange<?>) range;
            List<T> values = new ArrayList<>();

            for (Class<?> aClass : downwardsTypeRange.getInclude()) {
                T value = downwardIndex.computeIfAbsent(aClass, k -> supplier.get());
                values.add(value);
            }

            return values;
        }

        if (range instanceof TypeRange.UpwardsTypeRange) {
            TypeRange.UpwardsTypeRange<?> upwardsTypeRange = (TypeRange.UpwardsTypeRange<?>) range;
            T value = upwardIndex.computeIfAbsent(upwardsTypeRange.getType(), k -> supplier.get());

            return Collections.singletonList(value);
        }

        return Collections.emptyList();
    }

}
