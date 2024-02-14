package dev.rollczi.example.fabric;

import dev.rollczi.example.fabric.command.BanCommand;
import dev.rollczi.litecommands.fabric.LiteFabricFactory;
import net.fabricmc.api.ModInitializer;

/**
 * 2024/2/13<br>
 * LiteCommands<br>
 *
 * @author huanmeng_qwq
 */
public class ExampleFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        LiteFabricFactory.create()
            .commands(new BanCommand()).build();
    }
}
