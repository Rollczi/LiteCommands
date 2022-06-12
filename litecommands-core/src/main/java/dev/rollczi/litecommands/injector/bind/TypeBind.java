package dev.rollczi.litecommands.injector.bind;

import java.lang.reflect.Parameter;

public interface TypeBind<T> {

    T extract(Parameter parameter);

}
