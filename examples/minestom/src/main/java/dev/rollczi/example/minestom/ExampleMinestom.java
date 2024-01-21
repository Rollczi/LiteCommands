package dev.rollczi.example.minestom;


import dev.rollczi.example.minestom.command.MsgCommand;
import dev.rollczi.example.minestom.handler.MyInvalidUsageHandler;
import dev.rollczi.example.minestom.handler.MyPermissionMessage;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.minestom.LiteMinestomFactory;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;

public class ExampleMinestom {

    public static void main(String[] args) {
        MinecraftServer init = MinecraftServer.init();
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        // Create the instance
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
        // Set the ChunkGenerator
        instanceContainer.setGenerator(generationUnit -> generationUnit.modifier().fill(
            new Pos(generationUnit.absoluteStart().x(), 0, generationUnit.absoluteStart().z()),
            new Pos(generationUnit.absoluteEnd().x(), 160, generationUnit.absoluteEnd().z()),
            Block.STONE
        ));

        // Add an event callback to specify the spawning instance (and the spawn position)
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 160, 0));
        });


        // LiteCommands
        LiteCommands<CommandSender> liteCommands = LiteMinestomFactory.builder()
            .commands(
                new MsgCommand()
            )

            .missingPermission(new MyPermissionMessage())
            .invalidUsage(new MyInvalidUsageHandler())

            .build();

        init.start("localhost", 25565);
    }

}
