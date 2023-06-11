package dev.rollczi.litecommands.builder;

import dev.rollczi.litecommands.argument.parser.ArgumentParserRegistry;
import dev.rollczi.litecommands.bind.BindRegistry;
import dev.rollczi.litecommands.builder.processor.LiteBuilderPostProcessor;
import dev.rollczi.litecommands.builder.processor.LiteBuilderPreProcessor;
import dev.rollczi.litecommands.context.ContextRegistry;
import dev.rollczi.litecommands.result.ResultService;
import dev.rollczi.litecommands.command.builder.CommandBuilderCollector;
import dev.rollczi.litecommands.editor.EditorService;
import dev.rollczi.litecommands.platform.PlatformSettings;
import dev.rollczi.litecommands.platform.Platform;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.suggestion.SuggesterRegistry;
import dev.rollczi.litecommands.validator.ValidatorService;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface LiteCommandsInternalBuilderApi<SENDER, C extends PlatformSettings> {

    @ApiStatus.Internal
    Class<SENDER> getSenderClass();

    @ApiStatus.Internal
    Platform<SENDER, C> getPlatform();

    @ApiStatus.Internal
    LiteBuilderPreProcessor<SENDER, C> getPreProcessor();

    @ApiStatus.Internal
    LiteBuilderPostProcessor<SENDER, C> getPostProcessor();

    @ApiStatus.Internal
    Scheduler getScheduler();

    @ApiStatus.Internal
    EditorService<SENDER> getEditorService();

    @ApiStatus.Internal
    ValidatorService<SENDER> getValidatorService();

    @ApiStatus.Internal
    ArgumentParserRegistry<SENDER> getArgumentParserService();

    @ApiStatus.Internal
    SuggesterRegistry<SENDER> getSuggesterRegistry();

    @ApiStatus.Internal
    BindRegistry<SENDER> getBindRegistry();

    @ApiStatus.Internal
    ContextRegistry<SENDER> getContextRegistry();

    @ApiStatus.Internal
    WrapperRegistry getWrapperRegistry();

    @ApiStatus.Internal
    ResultService<SENDER> getResultService();

    @ApiStatus.Internal
    CommandBuilderCollector<SENDER> getCommandBuilderCollector();

}
