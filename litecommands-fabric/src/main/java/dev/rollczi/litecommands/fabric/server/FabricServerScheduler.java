package dev.rollczi.litecommands.fabric.server;

import dev.rollczi.litecommands.fabric.FabricScheduler;
import java.util.concurrent.ExecutorService;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;

import org.jetbrains.annotations.Nullable;

public class FabricServerScheduler extends FabricScheduler<TickTask> {

    private static @Nullable MinecraftServer CURRENT_SERVER;

    static {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> CURRENT_SERVER = server);
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> CURRENT_SERVER = null);
    }

    public FabricServerScheduler() {
        super();
    }

    public FabricServerScheduler(ExecutorService asyncExecutor) {
        super(asyncExecutor);
    }

    @Override
    public ReentrantBlockableEventLoop<TickTask> getMainThreadExecutor() {
        if (CURRENT_SERVER == null) {
            throw new IllegalStateException("Server is not started!");
        }

        return CURRENT_SERVER;
    }

}
