package dev.rollczi.litecommands.inject;

import dev.rollczi.litecommands.valid.ValidationCommandException;

import java.util.List;

public interface SingleArgumentHandler<T> {

    T parse(String argument) throws ValidationCommandException;

    List<String> tabulation(String command, String[] args);

}
