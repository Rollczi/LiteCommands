package dev.rollczi.litecommands.reflect.type;

import java.util.List;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class ParameterizedTypeToken<T> extends TypeToken<T> {
    
    private final Class<T> rawType;
    private final List<Class<?>> parametrizedTypes;

    public ParameterizedTypeToken(Class<T> rawType, List<Class<?>> parametrizedTypes) {
        super(rawType);
        this.rawType = rawType;
        this.parametrizedTypes = parametrizedTypes;
    }

    public List<Class<?>> getParametrizedTypes() {
        return parametrizedTypes;
    }

    @Override
    public TypeToken<?> getParameterized(int index) {
        if (index >= parametrizedTypes.size()) {
            throw new IllegalStateException("Cannot resolve parameterized type at index " + index);
        }

        return TypeToken.of(parametrizedTypes.get(index));
    }

}
