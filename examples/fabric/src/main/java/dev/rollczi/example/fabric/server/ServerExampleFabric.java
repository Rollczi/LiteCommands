package dev.rollczi.example.fabric.server;

import dev.rollczi.example.fabric.server.command.BanCommand;
import dev.rollczi.litecommands.fabric.LiteFabricFactory;
import net.fabricmc.api.ModInitializer;

public class ServerExampleFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        LiteFabricFactory.server()
            .commands(new BanCommand())
            .build();
    }
}
