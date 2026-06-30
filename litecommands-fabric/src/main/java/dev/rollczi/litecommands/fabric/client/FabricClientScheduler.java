package dev.rollczi.litecommands.fabric.client;

import dev.rollczi.litecommands.fabric.FabricScheduler;
import java.util.concurrent.ExecutorService;
import net.minecraft.client.Minecraft;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;

public class FabricClientScheduler extends FabricScheduler<Runnable> {

    public FabricClientScheduler() {
        super();
    }

    public FabricClientScheduler(ExecutorService asyncExecutor) {
        super(asyncExecutor);
    }

    @Override
    public ReentrantBlockableEventLoop<Runnable> getMainThreadExecutor() {
        return Minecraft.getInstance();
    }

}
