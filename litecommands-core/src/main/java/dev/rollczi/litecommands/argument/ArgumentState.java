package dev.rollczi.litecommands.argument;

import panda.std.Option;

import java.util.Optional;

public interface ArgumentState {

    Argument<?> argument();

    Option<String> name();

    Option<String> scheme();

}
