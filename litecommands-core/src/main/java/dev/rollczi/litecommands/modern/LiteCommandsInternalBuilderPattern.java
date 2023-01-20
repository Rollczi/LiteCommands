package dev.rollczi.litecommands.modern;

import dev.rollczi.litecommands.modern.command.CommandExecuteResultResolver;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentService;
import dev.rollczi.litecommands.modern.command.contextual.warpped.WrappedExpectedContextualService;
import dev.rollczi.litecommands.modern.platform.Platform;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public interface LiteCommandsInternalBuilderPattern<SENDER> {

    @ApiStatus.Internal
    Class<SENDER> getSenderClass();

    @ApiStatus.Internal
    ArgumentService<SENDER> getArgumentService();

    @ApiStatus.Internal
    CommandExecuteResultResolver<SENDER> getResultResolver();

    @ApiStatus.Internal
    WrappedExpectedContextualService getWrappedArgumentService();

    @Nullable
    @ApiStatus.Internal
    Platform<SENDER> getPlatform();

}
