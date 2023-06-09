package dev.rollczi.litecommands.scheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ScheduledChain<CHAIN extends ScheduledChainLink<? extends T>, T, R> {

    private final List<CHAIN> chain = new ArrayList<>();
    private final Function<T, R> mapper;

    private ScheduledChain(Collection<CHAIN> chain, Function<T, R> mapper) {
        this.chain.addAll(chain);
        this.mapper = mapper;
    }

    public CompletableFuture<List<R>> call(Scheduler scheduler) {
        ChainCollector collector = new ChainCollector(scheduler);

        return collector.collect();
    }

    public static <CHAIN extends ScheduledChainLink<T>, T> Builder<CHAIN, T> builder() {
        return new Builder<>();
    }

    public static class Builder<CHAIN extends ScheduledChainLink<T>, T> {

        private final List<CHAIN> chain = new ArrayList<>();

        public Builder<CHAIN, T> link(CHAIN link) {
            chain.add(link);
            return this;
        }

        public Builder<CHAIN, T> links(Collection<CHAIN> links) {
            chain.addAll(links);
            return this;
        }

        public <R> ScheduledChain<CHAIN, T, R> build(Function<T, R> mapper) {
            return new ScheduledChain<>(chain, mapper);
        }

    }

    private class ChainCollector {

        private final Iterator<CHAIN> chainLinkIterator;
        private final List<R> results = new ArrayList<>();
        private final Scheduler scheduler;

        ChainCollector(Scheduler scheduler) {
            this.scheduler = scheduler;
            this.chainLinkIterator = chain.iterator();
        }

        CompletableFuture<List<R>> collect() {
            return collect(new CompletableFuture<>());
        }

        private CompletableFuture<List<R>> collect(CompletableFuture<List<R>> future) {
            if (!chainLinkIterator.hasNext()) {
                future.complete(results);
                return future;
            }

            ScheduledChainLink<? extends T> chainLink = chainLinkIterator.next();

            scheduler.schedule(chainLink.type(), () -> {
                try {
                    T result = chainLink.call();

                    results.add(mapper.apply(result));
                }
                catch (ScheduledChainException exception) {
                    if (!chainLink.equals(exception.getLink())) {
                        future.completeExceptionally(new ScheduledChainException(exception.getCause(), chainLink));
                    }

                    future.completeExceptionally(exception);
                }
                catch (Throwable throwable) {
                    future.completeExceptionally(new ScheduledChainException(throwable, chainLink));
                }

                this.collect(future);
            });

            return future;
        }

    }

}
