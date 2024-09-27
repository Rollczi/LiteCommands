package dev.rollczi.litecommands.bind;

import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.wrapper.WrapFormat;

import java.util.function.Supplier;

public interface BindRequirement<PARSED> extends Requirement<PARSED> {

    static <T> BindRequirement<T> of(Supplier<String> name, Class<T> type) {
        return new BindRequirementImpl<>(name, WrapFormat.notWrapped(type));
    }

    static <T> BindRequirement<T> of(Supplier<String> name, WrapFormat<T, ?> format) {
        return new BindRequirementImpl<>(name, format);
    }

}
