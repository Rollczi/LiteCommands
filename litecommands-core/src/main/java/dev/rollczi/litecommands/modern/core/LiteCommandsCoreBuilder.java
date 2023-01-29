package dev.rollczi.litecommands.modern.core;

import dev.rollczi.litecommands.modern.LiteCommandsBaseBuilder;

import dev.rollczi.litecommands.modern.argument.ArgumentService;
import dev.rollczi.litecommands.modern.bind.BindRegistry;
import dev.rollczi.litecommands.modern.command.CommandExecuteResultResolver;
import dev.rollczi.litecommands.modern.command.editor.CommandEditorContextRegistry;
import dev.rollczi.litecommands.modern.command.editor.CommandEditorService;
import dev.rollczi.litecommands.modern.command.filter.CommandFilterService;
import dev.rollczi.litecommands.modern.contextual.warpped.WrappedExpectedContextualService;
import dev.rollczi.litecommands.modern.platform.Platform;
import org.jetbrains.annotations.Nullable;

public class LiteCommandsCoreBuilder<SENDER, B extends LiteCommandsCoreBuilder<SENDER, B>> extends LiteCommandsBaseBuilder<SENDER, B> {

    public LiteCommandsCoreBuilder(Class<SENDER> senderClass) {
        this(senderClass, null);
    }

    public LiteCommandsCoreBuilder(Class<SENDER> senderClass, Platform<SENDER> platform) {
        super(senderClass, platform);
    }

    public LiteCommandsCoreBuilder(LiteCommandsInternalPattern<SENDER> pattern) {
        super(pattern);
    }

    public LiteCommandsCoreBuilder(Class<SENDER> senderClass, CommandEditorService commandEditorService, CommandFilterService<SENDER> commandFilterService, ArgumentService<SENDER> argumentService, BindRegistry<SENDER> bindRegistry, WrappedExpectedContextualService wrappedExpectedContextualService, CommandExecuteResultResolver<SENDER> resultResolver, CommandEditorContextRegistry commandEditorContextRegistry, @Nullable Platform<SENDER> platform) {
        super(senderClass, commandEditorService, commandFilterService, argumentService, bindRegistry, wrappedExpectedContextualService, resultResolver, commandEditorContextRegistry, platform);
    }

}
