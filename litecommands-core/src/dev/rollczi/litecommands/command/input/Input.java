package dev.rollczi.litecommands.command.input;

import java.util.List;

public interface Input<MATCHER extends InputMatcher> {

    MATCHER createMatcher();

    default String[] asArray() {
        return asList().toArray(new String[0]);
    }

    List<String> asList();

}
