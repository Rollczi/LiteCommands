package dev.rollczi.litecommands.context;

import dev.rollczi.litecommands.reflect.type.TypeToken;
import dev.rollczi.litecommands.requirement.Requirement;

import java.util.function.Supplier;

public interface ContextRequirement<PARSED> extends Requirement<PARSED> {

    static <T> ContextRequirement<T> of(Supplier<String> name, Class<T> type) {
        return new ContextRequirementImpl<>(name, TypeToken.of(type));
    }

    static <T> ContextRequirement<T> of(Supplier<String> name, TypeToken<T> type) {
        return new ContextRequirementImpl<>(name, type);
    }

}
