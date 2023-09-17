package dev.rollczi.litecommands.programmatic;

import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.requirement.RequirementMatch;
import dev.rollczi.litecommands.requirement.RequirementsResult;
import dev.rollczi.litecommands.wrapper.Wrap;
import dev.rollczi.litecommands.wrapper.WrapFormat;

import java.util.Optional;

public class LiteContext<SENDER> {

    private final RequirementsResult<SENDER> result;

    public LiteContext(RequirementsResult<SENDER> result) {
        this.result = result;
    }

    public <T> T argument(String name, Class<T> type) {
        return this.get(name, WrapFormat.notWrapped(type));
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> argumentOptional(String name, Class<T> type) {
        return (Optional<T>) this.get(name, WrapFormat.of(type, Optional.class));
    }

    public boolean argumentFlag(String name) {
        return this.get(name, WrapFormat.notWrapped(Boolean.class));
    }

    public String argumentJoin(String name) {
        return this.get(name, WrapFormat.notWrapped(String.class));
    }

    public <T> T context(String name, Class<T> type) {
        return this.get(name, WrapFormat.notWrapped(type));
    }

    public Invocation<SENDER> invocation() {
        return result.getInvocation();
    }

    @SuppressWarnings("unchecked")
    private <PARSED, OUT> OUT get(String name, WrapFormat<PARSED, OUT> format) {
        RequirementMatch<SENDER, ?, ?> match = result.get(name);

        if (match == null) {
            throw new IllegalArgumentException("Argument with name '" + name + "' not found");
        }

        Wrap<PARSED> wrap = (Wrap<PARSED>) match.getResult();

        if (wrap.getParsedType() != format.getParsedType()) {
            throw new IllegalArgumentException("Argument with name '" + name + "' is not instance of " + format.getParsedType().getName() + " but " + wrap.getParsedType().getName());
        }

        Object unwrap = wrap.unwrap();

        if (unwrap.getClass() != format.getOutTypeOrParsed()) {
            throw new IllegalArgumentException("Argument with name '" + name + "' is not instance of " + format.getOutTypeOrParsed().getName() + " but " + unwrap.getClass().getName());
        }

        return (OUT) unwrap;
    }

}
