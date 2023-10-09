package dev.rollczi.litecommands.validator;

import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.MetaHolder;

public interface Validator<SENDER> {

    Flow validate(Invocation<SENDER> invocation, MetaHolder metaHolder);

}
