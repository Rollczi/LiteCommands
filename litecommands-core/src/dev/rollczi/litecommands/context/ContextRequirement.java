package dev.rollczi.litecommands.context;

import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.wrapper.WrapFormat;

import java.util.function.Supplier;

public interface ContextRequirement<PARSED> extends Requirement<PARSED> {

    static <T> ContextRequirement<T> of(Supplier<String> name, Class<T> type) {
        return new ContextRequirementImpl<>(name, WrapFormat.notWrapped(type));
    }

    static <T> ContextRequirement<T> of(Supplier<String> name, WrapFormat<T, ?> format) {
        return new ContextRequirementImpl<>(name, format);
    }

}
