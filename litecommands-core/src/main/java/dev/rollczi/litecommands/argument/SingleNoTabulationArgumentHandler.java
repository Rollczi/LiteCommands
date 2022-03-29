package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.valid.ValidationCommandException;
import dev.rollczi.litecommands.valid.ValidationInfo;
import panda.std.Option;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

public class SingleNoTabulationArgumentHandler<T> implements SingleArgumentHandler<T> {

    private final String name;
    private final BiFunction<LiteInvocation, String, T> function;

    public SingleNoTabulationArgumentHandler(String name, BiFunction<LiteInvocation, String, T> function) {
        this.name = name;
        this.function = function;
    }

    @Override
    public T parse(LiteInvocation invocation, String argument) throws ValidationCommandException {
        return Option.attempt(Throwable.class, () -> function.apply(invocation, argument))
                .orThrow(() -> new ValidationCommandException(ValidationInfo.INVALID_USE));
    }

    @Override
    public List<String> tabulation(LiteInvocation invocation, String command, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getName() {
        return this.name;
    }

}
