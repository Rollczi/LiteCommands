package dev.rollczi.litecommands.paper;

import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.command.CommandManager;
import dev.rollczi.litecommands.editor.CommandEditorContext;
import dev.rollczi.litecommands.editor.CommandEditorContextRegistry;
import dev.rollczi.litecommands.editor.CommandEditorExecutorBuilder;
import dev.rollczi.litecommands.test.TestExecutor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class LitePaperExtensionTest {

    @Test
    void test() {
        Server server = mock(Server.class);

        LiteCommands<CommandSender> liteCommands = LiteBukkitFactory.builder(server)
                .extension(new LitePaperExtension<>())
                .settings(settings -> settings.commandsProvider(new TestBukkitCommandProvider()))
                .extension((builder, pattern) -> {
                    CommandEditorContextRegistry<CommandSender> registry = pattern.getCommandContextRegistry();

                    registry.register(() -> CommandEditorContext.<CommandSender>create()
                            .name("test")
                            .appendExecutor(new CommandEditorExecutorBuilder<>(new TestExecutor<>()))
                    );
                })
                .register();

        CommandManager<CommandSender, ?> commandManager = liteCommands.getCommandManager();

        commandManager.unregisterAll();
    }

}