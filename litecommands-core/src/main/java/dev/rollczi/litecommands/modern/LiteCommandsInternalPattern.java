package dev.rollczi.litecommands.modern;

import dev.rollczi.litecommands.modern.argument.ArgumentService;
import dev.rollczi.litecommands.modern.bind.BindRegistry;
import dev.rollczi.litecommands.modern.command.CommandExecuteResultResolver;
import dev.rollczi.litecommands.modern.editor.CommandEditorContextRegistry;
import dev.rollczi.litecommands.modern.editor.CommandEditorService;
import dev.rollczi.litecommands.modern.validator.CommandValidatorService;
import dev.rollczi.litecommands.modern.wrapper.WrappedExpectedService;
import dev.rollczi.litecommands.modern.platform.Platform;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public interface LiteCommandsInternalPattern<SENDER, C extends LiteConfiguration> {

    @ApiStatus.Internal
    Class<SENDER> getSenderClass();

    @ApiStatus.Internal
    CommandEditorService<SENDER> getCommandEditorService();

    @ApiStatus.Internal
    CommandValidatorService<SENDER> getCommandFilterService();

    @ApiStatus.Internal
    ArgumentService<SENDER> getArgumentService();

    @ApiStatus.Internal
    BindRegistry<SENDER> getBindRegistry();

    @ApiStatus.Internal
    WrappedExpectedService getWrappedExpectedContextualService();

    @ApiStatus.Internal
    CommandExecuteResultResolver<SENDER> getResultResolver();

    @ApiStatus.Internal
    CommandEditorContextRegistry<SENDER> getCommandContextRegistry();

    @Nullable
    @ApiStatus.Internal
    Platform<SENDER> getPlatform();

    @NotNull
    @ApiStatus.Internal
    C getConfiguration();

}
