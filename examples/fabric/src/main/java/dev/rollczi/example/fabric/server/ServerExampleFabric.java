package dev.rollczi.example.fabric.server;

import dev.rollczi.example.fabric.server.command.ExampleCommand;
import dev.rollczi.litecommands.fabric.LiteFabricFactory;
import dev.rollczi.litecommands.luckperms.LuckPermsPermissionFactory;
import net.fabricmc.api.DedicatedServerModInitializer;

public class ServerExampleFabric implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        LiteFabricFactory.server()
            .permissionResolver(new LuckPermsPermissionFactory())
            .commands(new ExampleCommand())
            .build();
    }
}
