package dev.rollczi.litecommands.builder;

import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.bind.BindRegistry;
import dev.rollczi.litecommands.context.ContextRegistry;
import dev.rollczi.litecommands.handler.result.ResultHandleService;
import dev.rollczi.litecommands.command.builder.CommandBuilderCollector;
import dev.rollczi.litecommands.editor.EditorService;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.platform.PlatformSettings;
import dev.rollczi.litecommands.platform.Platform;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
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
    Scheduler getScheduler();

    @ApiStatus.Internal
    EditorService<SENDER> getEditorService();

    @ApiStatus.Internal
    ValidatorService<SENDER> getValidatorService();

    @ApiStatus.Internal
    ParserRegistry<SENDER> getParserRegistry();

    @ApiStatus.Internal
    SuggesterRegistry<SENDER> getSuggesterRegistry();

    @ApiStatus.Internal
    BindRegistry getBindRegistry();

    @ApiStatus.Internal
    ContextRegistry<SENDER> getContextRegistry();

    @ApiStatus.Internal
    WrapperRegistry getWrapperRegistry();

    @ApiStatus.Internal
    ResultHandleService<SENDER> getResultService();

    @ApiStatus.Internal
    CommandBuilderCollector<SENDER> getCommandBuilderCollector();

    @ApiStatus.Internal
    MessageRegistry<SENDER> getMessageRegistry();

}
