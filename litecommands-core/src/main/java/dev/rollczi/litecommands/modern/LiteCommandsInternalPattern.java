package dev.rollczi.litecommands.modern;

import dev.rollczi.litecommands.modern.argument.ArgumentService;
import dev.rollczi.litecommands.modern.bind.BindRegistry;
import dev.rollczi.litecommands.modern.command.CommandExecuteResultResolver;
import dev.rollczi.litecommands.modern.command.editor.CommandEditorContextRegistry;
import dev.rollczi.litecommands.modern.command.editor.CommandEditorService;
import dev.rollczi.litecommands.modern.command.filter.CommandFilterService;
import dev.rollczi.litecommands.modern.contextual.warpped.WrappedExpectedContextualService;
import dev.rollczi.litecommands.modern.platform.Platform;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public interface LiteCommandsInternalPattern<SENDER> {

    @ApiStatus.Internal
    Class<SENDER> getSenderClass();

    @ApiStatus.Internal
    CommandEditorService<SENDER> getCommandEditorService();

    @ApiStatus.Internal
    CommandFilterService<SENDER> getCommandFilterService();

    @ApiStatus.Internal
    ArgumentService<SENDER> getArgumentService();

    @ApiStatus.Internal
    BindRegistry<SENDER> getBindRegistry();

    @ApiStatus.Internal
    WrappedExpectedContextualService getWrappedExpectedContextualService();

    @ApiStatus.Internal
    CommandExecuteResultResolver<SENDER> getResultResolver();

    @ApiStatus.Internal
    CommandEditorContextRegistry<SENDER> getCommandContextRegistry();

    @Nullable
    @ApiStatus.Internal
    Platform<SENDER> getPlatform();

}
