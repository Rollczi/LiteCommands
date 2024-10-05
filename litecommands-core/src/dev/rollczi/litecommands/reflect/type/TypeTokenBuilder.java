package dev.rollczi.litecommands.reflect.type;

import java.util.ArrayList;
import java.util.List;

public class TypeTokenBuilder<T> {
    
    private final Class<T> rawType;
    private final List<Class<?>> parametrizedTypes = new ArrayList<>();
    
    public TypeTokenBuilder(Class<T> rawType) {
        this.rawType = rawType;
    }
    
    public TypeTokenBuilder<T> parametrized(Class<?> parametrizedType) {
        this.parametrizedTypes.add(parametrizedType);
        return this;
    }
    
    public TypeToken<T> build() {
        return new ParameterizedTypeToken<>(rawType, parametrizedTypes);
    }
    
}
