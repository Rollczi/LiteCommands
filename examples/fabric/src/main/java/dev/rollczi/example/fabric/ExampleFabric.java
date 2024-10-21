package dev.rollczi.example.fabric;

import dev.rollczi.example.fabric.command.BanCommand;
import dev.rollczi.litecommands.fabric.LiteFabricFactory;
import net.fabricmc.api.ModInitializer;


public class ExampleFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        LiteFabricFactory.server()
            .commands(new BanCommand())
            .build();
    }
}
