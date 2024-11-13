package dev.rollczi.litecommands.scheduler;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SchedulerExecutorPoolBuilder {

    private ScheduledExecutorService mainExecutor;
    private boolean closeMainExecutorOnShutdown;

    private ScheduledExecutorService asyncExecutor;
    private boolean closeAsyncExecutorOnShutdown;

    public void setMainExecutor(ScheduledExecutorService mainExecutor, boolean closeOnShutdown) {
        this.mainExecutor = mainExecutor;
        this.closeMainExecutorOnShutdown = closeOnShutdown;
    }

    public void setAsyncExecutor(ScheduledExecutorService asyncExecutor, boolean closeOnShutdown) {
        this.asyncExecutor = asyncExecutor;
        this.closeAsyncExecutorOnShutdown = closeOnShutdown;
    }

    public SchedulerExecutorPoolImpl build() {
        if (mainExecutor == null) {
            /*
             In some cases, the main executor might never be used. We don't want to have to create a useless thread

             This may look like a premature optimization, but a typical server has 40+ plugins
             As the framework spreads, more and more of them can use LiteCommands,
             so we better handle this situation, there are practically no losses anyway
             */
            ScheduledThreadPoolExecutor mainExecutor = new ScheduledThreadPoolExecutor(1);
            mainExecutor.allowCoreThreadTimeOut(true);
            mainExecutor.setKeepAliveTime(1, TimeUnit.HOURS);

            this.mainExecutor = mainExecutor;
            closeMainExecutorOnShutdown = true;
        }

        if (asyncExecutor == null) {
            /*
            We want to create a thread pool that both is fast and uses as few threads as possible.
            So we use a fixed core pool size to not hold an unreasonable number of threads on server CPUs

            We could also decrease the max cap, but long-running commands are those that use I/O,
            so it's fine and meaningful to create a bigger pool in this case
            */
            ScheduledThreadPoolExecutor asyncExecutor = new ScheduledThreadPoolExecutor(4);
            asyncExecutor.setMaximumPoolSize(Runtime.getRuntime().availableProcessors() * 2);
            asyncExecutor.setKeepAliveTime(3, TimeUnit.MINUTES);

            this.asyncExecutor = asyncExecutor;
            closeAsyncExecutorOnShutdown = true;
        }

        return new SchedulerExecutorPoolImpl(mainExecutor, closeMainExecutorOnShutdown, asyncExecutor, closeAsyncExecutorOnShutdown);
    }
}
