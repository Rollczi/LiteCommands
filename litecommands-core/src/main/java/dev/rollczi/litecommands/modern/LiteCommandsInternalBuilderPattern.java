package dev.rollczi.litecommands.modern;

import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResolverRegistry;
import org.jetbrains.annotations.ApiStatus;

public interface LiteCommandsInternalBuilderPattern<SENDER> {

    @ApiStatus.Internal
    ArgumentResolverRegistry<SENDER> getArgumentResolver();

}
