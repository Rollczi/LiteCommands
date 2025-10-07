package dev.rollczi.example.fabric.client;

import dev.rollczi.example.fabric.client.command.ClientCommands;
import dev.rollczi.example.fabric.client.command.DebugClassInfoCommand;
import dev.rollczi.litecommands.fabric.LiteFabricFactory;
import net.fabricmc.api.ClientModInitializer;

public class ClientExampleFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LiteFabricFactory.client()
            .commands(new ClientCommands(), new DebugClassInfoCommand())
            .settings(settings -> settings.inputInspectionDisplay("[....]"))
            .build();
    }
}
