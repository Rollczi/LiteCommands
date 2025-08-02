package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.minestom.test.RegisterCommand;
import dev.rollczi.litecommands.minestom.test.TestPlayer;
import dev.rollczi.litecommands.minestom.test.TestPlayerConnection;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.entity.Player;
import net.minestom.server.network.ConnectionManager;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.timer.SchedulerManager;
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
        System.setProperty("minestom.inside-test", "true");
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

        MinecraftServer.getConnectionManager().setPlayerProvider(
            (connection, gameProfile) -> new TestPlayer(connection, gameProfile));

        liteCommands = LiteMinestomFactory.builder()
            .commands(commands.values().toArray())
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

    protected static TestPlayer player(String name) {
        ConnectionManager connectionManager = MinecraftServer.getConnectionManager();
        GameProfile gameProfile = new GameProfile(UUID.nameUUIDFromBytes(name.getBytes()), name);
        Player player = connectionManager.createPlayer(new TestPlayerConnection(), gameProfile);
        connectionManager.transitionConfigToPlay(player);
        connectionManager.tick(0);

        return (TestPlayer) player;
    }

    protected static ConsoleSender console() {
        return MinecraftServer.getCommandManager().getConsoleSender();
    }

    protected static void executeCommand(CommandSender sender, String command) {
        SchedulerManager connectionManager = MinecraftServer.getSchedulerManager();
        MinecraftServer.getCommandManager().execute(sender, command);
        connectionManager.processTick(); // Process the command
    }

}
