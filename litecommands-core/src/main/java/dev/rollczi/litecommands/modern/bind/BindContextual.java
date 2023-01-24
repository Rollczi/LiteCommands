package dev.rollczi.litecommands.modern.bind;

import dev.rollczi.litecommands.modern.invocation.Invocation;

import java.util.function.Function;

public interface BindContextual<SENDER, T> extends Function<Invocation<SENDER>, T> {

}
