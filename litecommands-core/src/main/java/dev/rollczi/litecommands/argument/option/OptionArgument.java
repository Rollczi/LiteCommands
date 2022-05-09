package dev.rollczi.litecommands.argument.option;

import dev.rollczi.litecommands.argument.SingleArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;
import panda.std.Option;

import java.util.Collections;
import java.util.List;

public class OptionArgument<T> implements SingleArgument<Opt> {

    private final Class<T> type;
    private final OptionalArgumentSupplier<T> supplier;

    public OptionArgument(Class<T> type, OptionalArgumentSupplier<T> supplier) {
        this.type = type;
        this.supplier = supplier;
    }

    @Override
    public MatchResult match(LiteInvocation invocation, Opt annotation, int currentRoute, int currentArgument, String argument) {
        if (!annotation.value().equals(type)) {
            return MatchResult.notMatched();
        }

        return MatchResult.matched(supplier.get(invocation, argument), 1);
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public List<Object> getDefaultValue() {
        return Collections.singletonList(Option.none());
    }

}
