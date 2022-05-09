package dev.rollczi.litecommands.argument.option;

import dev.rollczi.litecommands.command.LiteInvocation;
import panda.std.Option;

@FunctionalInterface
public interface OptionalArgumentSupplier<T> {

    Option<T> get(LiteInvocation invocation, String argument);

}
