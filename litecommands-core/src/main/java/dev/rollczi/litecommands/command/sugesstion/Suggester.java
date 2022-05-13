package dev.rollczi.litecommands.command.sugesstion;

import java.util.Collections;
import java.util.List;

public interface Suggester {

    Suggester NONE = Collections::emptyList;

    List<Suggestion> suggestions();

}
