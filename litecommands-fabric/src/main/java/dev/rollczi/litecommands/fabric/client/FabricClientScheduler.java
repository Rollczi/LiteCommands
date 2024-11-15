package dev.rollczi.litecommands.fabric.client;

import dev.rollczi.litecommands.fabric.FabricScheduler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.thread.ReentrantThreadExecutor;

public class FabricClientScheduler extends FabricScheduler<Runnable> {
    public FabricClientScheduler() {
        this(-1);
    }

    public FabricClientScheduler(int pool) {
        super(pool);
    }

    @Override
    public ReentrantThreadExecutor<Runnable> getMainThreadExecutor() {
        return MinecraftClient.getInstance();
    }
}
