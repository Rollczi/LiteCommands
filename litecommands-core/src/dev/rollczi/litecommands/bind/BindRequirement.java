package dev.rollczi.litecommands.bind;

import dev.rollczi.litecommands.reflect.type.TypeToken;
import dev.rollczi.litecommands.requirement.Requirement;

import java.util.function.Supplier;

public interface BindRequirement<PARSED> extends Requirement<PARSED> {

    static <T> BindRequirement<T> of(Supplier<String> name, Class<T> type) {
        return new BindRequirementImpl<>(name, TypeToken.of(type));
    }

    static <T> BindRequirement<T> of(Supplier<String> name, TypeToken<T> type) {
        return new BindRequirementImpl<>(name, type);
    }

}
