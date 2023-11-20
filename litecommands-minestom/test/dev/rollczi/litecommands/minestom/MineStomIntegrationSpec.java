package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.minestom.test.RegisterCommand;
import dev.rollczi.litecommands.minestom.test.TestPlayer;
import dev.rollczi.litecommands.minestom.tools.MinestomOnlyPlayerContext;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.entity.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MineStomIntegrationSpec {

    private static final Map<Field, Object> commands = new HashMap<>();

    protected static MinecraftServer minecraftServer;
    protected static LiteCommands<CommandSender> liteCommands;

    @BeforeAll
    static void beforeAll(TestInfo testInfo) throws ReflectiveOperationException {
        minecraftServer = MinecraftServer.init();

        Class<?> type =  testInfo.getTestClass().orElseThrow();

        for (Field field : type.getDeclaredFields()) {
            RegisterCommand registerCommand = field.getAnnotation(RegisterCommand.class);

            if (registerCommand == null) {
                return;
            }

            Class<?> commandType = field.getType();
            Constructor<?> constructor = commandType.getDeclaredConstructor();

            constructor.setAccessible(true);
            Object command = constructor.newInstance();
            commands.put(field, command);
        }

        liteCommands = LiteMinestomFactory.builder(MinecraftServer.getServer(), MinecraftServer.getCommandManager())
            .commands(commands.values().toArray())
            .context(Player.class, new MinestomOnlyPlayerContext<>(""))
            .build();
    }

    @BeforeEach
    void beforeEach() throws IllegalAccessException {
        for (Map.Entry<Field, Object> entry : commands.entrySet()) {
            Field field = entry.getKey();
            Object command = entry.getValue();

            field.setAccessible(true);
            field.set(this, command);
        }
    }

    protected static Player player(String name) {
        return new TestPlayer(UUID.nameUUIDFromBytes(name.getBytes()), name);
    }

    protected static Player player() {
        return player("Rollczi");
    }

    protected static ConsoleSender console() {
        return MinecraftServer.getCommandManager().getConsoleSender();
    }

    protected static void executeCommand(CommandSender sender, String command) {
        MinecraftServer.getCommandManager().execute(sender, command);
    }

}
