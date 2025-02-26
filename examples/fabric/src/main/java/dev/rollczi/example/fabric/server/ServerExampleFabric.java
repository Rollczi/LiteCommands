package dev.rollczi.example.fabric.server;

import dev.rollczi.example.fabric.server.command.ExampleCommand;
import dev.rollczi.litecommands.fabric.LiteFabricFactory;
import dev.rollczi.litecommands.luckperms.LuckPermsPermissionFactory;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;

public class ServerExampleFabric implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        LiteFabricFactory.server()
            .permissionResolver(LuckPermsPermissionFactory.create(ServerCommandSource.class, (sender) -> {
                if (sender.isExecutedByPlayer()) {
                    return sender.getPlayer().getUuid();
                }

                if (sender.output instanceof MinecraftServer) {
                    return LuckPermsPermissionFactory.CONSOLE;
                }

                return null;
            }))
            .commands(new ExampleCommand())
            .build();
    }
}
