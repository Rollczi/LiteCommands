package dev.rollczi.litecommands.wrapper.std;

import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import dev.rollczi.litecommands.wrapper.Wrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@SuppressWarnings("rawtypes")
public class CompletableFutureWrapper extends AbstractWrapper<CompletableFuture> implements Wrapper {

    private final Scheduler scheduler;

    public CompletableFutureWrapper(Scheduler scheduler) {
        super(CompletableFuture.class);
        this.scheduler = scheduler;
    }

    @Override
    protected <EXPECTED> Supplier<CompletableFuture> wrapValue(EXPECTED valueToWrap, WrapFormat<EXPECTED, ?> info) {
        return () -> scheduler.supplyAsync(() -> valueToWrap);
    }

}
