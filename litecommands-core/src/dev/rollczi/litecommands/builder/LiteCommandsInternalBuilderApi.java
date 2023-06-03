package dev.rollczi.litecommands.builder;

import dev.rollczi.litecommands.argument.parser.ArgumentParserRegistry;
import dev.rollczi.litecommands.bind.BindRegistry;
import dev.rollczi.litecommands.builder.processor.LiteBuilderPostProcessor;
import dev.rollczi.litecommands.builder.processor.LiteBuilderPreProcessor;
import dev.rollczi.litecommands.command.CommandExecuteResultResolver;
import dev.rollczi.litecommands.editor.CommandEditorContextRegistry;
import dev.rollczi.litecommands.editor.CommandEditorService;
import dev.rollczi.litecommands.platform.LiteSettings;
import dev.rollczi.litecommands.platform.Platform;
import dev.rollczi.litecommands.validator.ValidatorService;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface LiteCommandsInternalBuilderApi<SENDER, C extends LiteSettings> {

    @ApiStatus.Internal
    Class<SENDER> getSenderClass();

    @ApiStatus.Internal
    Platform<SENDER, C> getPlatform();

    @ApiStatus.Internal
    LiteBuilderPreProcessor<SENDER, C> getPreProcessor();

    @ApiStatus.Internal
    LiteBuilderPostProcessor<SENDER, C> getPostProcessor();

    @ApiStatus.Internal
    CommandEditorService<SENDER> getCommandEditorService();

    @ApiStatus.Internal
    ValidatorService<SENDER> getCommandFilterService();

    @ApiStatus.Internal
    ArgumentParserRegistry<SENDER> getArgumentService();

    @ApiStatus.Internal
    BindRegistry<SENDER> getBindRegistry();

    @ApiStatus.Internal
    WrapperRegistry getWrappedExpectedContextualService();

    @ApiStatus.Internal
    CommandExecuteResultResolver<SENDER> getResultResolver();

    @ApiStatus.Internal
    CommandEditorContextRegistry<SENDER> getCommandContextRegistry();

}
