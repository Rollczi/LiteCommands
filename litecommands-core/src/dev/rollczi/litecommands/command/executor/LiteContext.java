package dev.rollczi.litecommands.command.executor;

import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import dev.rollczi.litecommands.requirement.RequirementMatch;
import dev.rollczi.litecommands.requirement.RequirementsResult;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class LiteContext<SENDER> {

    private final RequirementsResult<SENDER> result;
    private Object returnResult;

    public LiteContext(RequirementsResult<SENDER> result) {
        this.result = result;
    }

    public <T> T argument(String name, Class<T> type) {
        return this.get(name, TypeToken.of(type));
    }

    public String argumentQuoted(String name) {
        return this.argument(name, String.class);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> argumentOptional(String name, Class<T> type) {
        return (Optional<T>) this.get(name, TypeToken.of(Optional.class));
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> T argumentNullable(String name, Class<T> type) {
        Optional<T> optional = this.get(name, TypeToken.of(Optional.class));

        return optional.orElse(null);
    }

    public boolean argumentFlag(String name) {
        return Boolean.TRUE.equals(this.get(name, TypeToken.of(Boolean.class)));
    }

    public String argumentJoin(String name) {
        return this.get(name, TypeToken.of(String.class));
    }

    public <T> T context(String name, Class<T> type) {
        return this.get(name, TypeToken.of(type));
    }

    public <T> T context(String name, TypeToken<T> type) {
        return this.get(name, type);
    }

    public <T> T bind(String name, Class<T> type) {
        return this.get(name, TypeToken.of(type));
    }

    public <T> T bind(String name, TypeToken<T> type) {
        return this.get(name, type);
    }

    public Invocation<SENDER> invocation() {
        return result.getInvocation();
    }

    @SuppressWarnings("unchecked")
    private <T> T get(String name, TypeToken<T> format) {
        RequirementMatch match = result.get(name);

        if (match == null) {
            throw new IllegalArgumentException("Requirement with name '" + name + "' not found");
        }

        Object matchResult = match.getResult();

        if (matchResult == null) {
            return null;
        }

        if (!format.getRawType().isAssignableFrom(matchResult.getClass())) {
            throw new IllegalArgumentException("Argument with name '" + name + "' is not instance of " + format.getRawType().getName() + " but " + matchResult.getClass().getName());
        }
        return (T) matchResult;
    }

    @ApiStatus.Experimental
    public void returnResult(Object returnResult) {
        this.returnResult = returnResult;
    }

    @ApiStatus.Experimental
    Object getReturnResult() {
        return returnResult;
    }
}
