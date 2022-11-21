package dev.rollczi.litecommands.modern.argument.invocation;

import dev.rollczi.litecommands.modern.argument.ArgumentContext;

public interface ArgumentResolver<SENDER, ARGUMENT, TYPE, CONTEXT extends ArgumentContext<ARGUMENT, TYPE>> {

    ArgumentResultCollector<SENDER> resolve(CONTEXT context, ArgumentResultPreparedCollector<SENDER, ARGUMENT, TYPE, CONTEXT> collector);

}
