package dev.rollczi.litecommands.inject;

import dev.rollczi.litecommands.valid.ValidationCommandException;

import java.util.Collection;

public interface SingleArgumentHandler<T> {

    T parse(String argument) throws ValidationCommandException;

    Collection<String> tabulation(String command, String[] args);

}
