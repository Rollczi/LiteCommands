package dev.rollczi.litecommands.fabric.server;

import dev.rollczi.litecommands.fabric.FabricScheduler;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
import net.minecraft.util.thread.ReentrantThreadExecutor;

import java.util.function.Supplier;

public class FabricServerScheduler extends FabricScheduler<ServerTask> {
    static {
        new ServerGetter();
    }

    public FabricServerScheduler() {
        this(-1);
    }

    public FabricServerScheduler(int pool) {
        super(new ServerGetter(), pool);
    }

    @Override
    public ReentrantThreadExecutor<ServerTask> getExecutor() {
        return ServerGetter.currentServer;
    }

    private static class ServerGetter implements Supplier<Boolean> {
        private static MinecraftServer currentServer;

        static {
            ServerLifecycleEvents.SERVER_STARTED.register(server -> ServerGetter.currentServer = server);
        }

        @Override
        public Boolean get() {
            if (currentServer != null) {
                return currentServer.isOnThread();
            }
            return false;
        }
    }
}
