package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.requirement.RequirementCondition;
import dev.rollczi.litecommands.requirement.RequirementResult;
import dev.rollczi.litecommands.shared.FailedReason;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import org.jetbrains.annotations.ApiStatus;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ParseCompletedResult<EXPECTED> implements ParseResult<EXPECTED>, RequirementResult<EXPECTED> {

    static final ParseResult<?> NULL_SUCCESS = new ParseCompletedResult<>(null, null, true, Collections.emptyList());

    private final @Nullable EXPECTED successfulResult;
    private final @Nullable FailedReason failedResult;
    private final List<RequirementCondition> conditions;
    private final boolean nullable;

    ParseCompletedResult(@Nullable EXPECTED successfulResult, @Nullable FailedReason failedResult, boolean nullable, List<RequirementCondition> conditions) {
        this.conditions = conditions;
        if (successfulResult != null && failedResult != null) {
            throw new IllegalArgumentException("Cannot be both successful and failed");
        }
        else if ((successfulResult == null && !nullable) && failedResult == null) {
            throw new IllegalArgumentException("Cannot be both empty");
        }

        this.nullable = nullable;
        this.successfulResult = successfulResult;
        this.failedResult = failedResult;
    }

    @Override
    public boolean isSuccessful() {
        return this.successfulResult != null;
    }

    @Override
    public boolean isSuccessfulNull() {
        return this.nullable && this.successfulResult == null;
    }

    @Override
    public boolean isFailed() {
        return this.failedResult != null;
    }

    @Override
    public @NotNull EXPECTED getSuccess() {
        if (this.successfulResult == null) {
            throw new IllegalStateException("Cannot get successful result when it is empty");
        }

        return this.successfulResult;
    }

    @Override
    public @NotNull Object getFailedReason() {
        if (this.failedResult == null) {
            throw new IllegalStateException("Cannot get failed reason when it is empty");
        }

        return this.failedResult.getReason();
    }

    @Override
    public @NotNull List<RequirementCondition> getConditions() {
        return conditions;
    }

    @ApiStatus.Experimental
    @Override
    public <R> ParseResult<R> map(Function<EXPECTED, R> mapper) {
        if (this.isFailed()) {
            return (ParseResult<R>) this;
        }

        return ParseResult.success(mapper.apply(getSuccess()));
    }

    @Override
    public ParseResult<EXPECTED> mapFailure(Function<Object, ParseResult<EXPECTED>> mapper) {
        if (this.isFailed()) {
            return mapper.apply(failedResult.getReason());
        }

        return this;
    }

    @ApiStatus.Experimental
    @Override
    public <R> ParseResult<R> flatMap(Function<EXPECTED, ParseResult<R>> mapper) {
        if (this.isFailed()) {
            return ParseResult.failure(failedResult);
        }

        return mapper.apply(getSuccess());
    }

    @Override
    public ParseCompletedResult<EXPECTED> whenSuccessful(Consumer<EXPECTED> action) {
        if (this.isSuccessful()) {
            action.accept(this.getSuccess());
        }

        return this;
    }

    @Override
    public ParseCompletedResult<EXPECTED> whenFailed(Consumer<FailedReason> action) {
        if (this.isFailed()) {
            action.accept(this.failedResult);
        }

        return this;
    }

    @Override
    public CompletableFuture<RequirementResult<EXPECTED>> asFuture() {
        return CompletableFuture.completedFuture(this);
    }

    @Override
    public RequirementResult<EXPECTED> asResultOr(RequirementResult<EXPECTED> result) {
        return this;
    }

    @Override
    public RequirementResult<EXPECTED> await() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParseCompletedResult<?> that = (ParseCompletedResult<?>) o;
        return Objects.equals(successfulResult, that.successfulResult) && Objects.equals(failedResult, that.failedResult);
    }

    @Override
    public int hashCode() {
        return Objects.hash(successfulResult, failedResult);
    }

    @Override
    public String toString() {
        return "ParseResult{" +
            "successfulResult=" + successfulResult +
            ", failedResult=" + failedResult +
            '}';
    }

}
