package dev.rollczi.litecommands.fabric.client;

import dev.rollczi.litecommands.fabric.FabricScheduler;
import java.util.concurrent.ExecutorService;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.thread.ReentrantThreadExecutor;

public class FabricClientScheduler extends FabricScheduler<Runnable> {

    public FabricClientScheduler() {
        super();
    }

    public FabricClientScheduler(ExecutorService asyncExecutor) {
        super(asyncExecutor);
    }

    @Override
    public ReentrantThreadExecutor<Runnable> getMainThreadExecutor() {
        return MinecraftClient.getInstance();
    }

}
