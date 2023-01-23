package dev.rollczi.litecommands.modern;

import dev.rollczi.litecommands.modern.command.CommandExecuteResultResolver;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentService;
import dev.rollczi.litecommands.modern.command.bind.BindRegistry;
import dev.rollczi.litecommands.modern.command.contextual.warpped.WrappedExpectedContextualService;
import dev.rollczi.litecommands.modern.command.editor.CommandEditorContextRegistry;
import dev.rollczi.litecommands.modern.command.editor.CommandEditorService;
import dev.rollczi.litecommands.modern.platform.Platform;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public interface LiteCommandsInternalPattern<SENDER> {

    @ApiStatus.Internal
    Class<SENDER> getSenderClass();

    @ApiStatus.Internal
    CommandEditorService getCommandEditorService();

    @ApiStatus.Internal
    ArgumentService<SENDER> getArgumentService();

    @ApiStatus.Internal
    BindRegistry<SENDER> getBindRegistry();

    @ApiStatus.Internal
    WrappedExpectedContextualService getWrappedExpectedContextualService();

    @ApiStatus.Internal
    CommandExecuteResultResolver<SENDER> getResultResolver();

    @ApiStatus.Internal
    CommandEditorContextRegistry getCommandContextRegistry();

    @Nullable
    @ApiStatus.Internal
    Platform<SENDER> getPlatform();

}
