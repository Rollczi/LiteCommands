package dev.rollczi.example.fabric.server;

import dev.rollczi.example.fabric.server.command.DoubleSlashCommand;
import dev.rollczi.example.fabric.server.command.ExampleCommand;
import dev.rollczi.litecommands.fabric.LiteFabricFactory;
import dev.rollczi.litecommands.luckperms.LuckPermsPermissionResolver;
import net.fabricmc.api.DedicatedServerModInitializer;

public class ServerExampleFabric implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        LiteFabricFactory.server()
            .permissionResolver(LuckPermsPermissionResolver.lazy())
            .commands(
                new ExampleCommand(),
                new DoubleSlashCommand()
            )
            .build();
    }
}
