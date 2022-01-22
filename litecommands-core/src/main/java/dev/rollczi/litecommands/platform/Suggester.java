package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.LiteInvocation;

import java.util.Collections;
import java.util.List;

public interface Suggester {

    List<String> suggest(LiteInvocation invocation);

    Suggester NONE = ignore -> Collections.emptyList();

}
