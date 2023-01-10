package dev.rollczi.litecommands.modern.command.argument.invocation.warpped.implementations;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResult;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpped.WrappedArgumentWrapper;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpped.WrappedArgumentFactory;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class CompletableFutureWrappedArgumentFactory implements WrappedArgumentFactory {

    @Override
    public <DETERMINANT, EXPECTED> WrappedArgumentWrapper<EXPECTED> wrap(
        ArgumentResult<EXPECTED> result,
        ArgumentContext<DETERMINANT, EXPECTED> context
    ) {
        Class<EXPECTED> expectedType = context.getExpectedType();

        if (result.isFailed()) {
            throw new IllegalArgumentException("Result can not be failed");
        }

        return new CompletableFutureWrapper<>(expectedType, result.getSuccessfulResult().getParsedArgument());
    }

    @Override
    public Class<?> getWrapperType() {
        return CompletableFuture.class;
    }

    private static class CompletableFutureWrapper<EXPECTED> implements WrappedArgumentWrapper<EXPECTED> {

        private final Class<EXPECTED> expectedType;
        private final CompletableFuture<EXPECTED> completableFuture;

        private CompletableFutureWrapper(Class<EXPECTED> expectedType, Supplier<EXPECTED> completableFuture) {
            this.expectedType = expectedType;
            this.completableFuture = CompletableFuture.supplyAsync(completableFuture);
        }

        @Override
        public Object unwrap() {
            return completableFuture;
        }

        @Override
        public Class<EXPECTED> getExpectedType() {
            return expectedType;
        }

    }

}
